package com.yqh.libcompiler

import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.types.asKotlinTypeName
import com.bennyhuo.aptutils.types.packageName
import com.bennyhuo.aptutils.types.simpleName
import com.bennyhuo.aptutils.utils.writeToFile
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.yqh.libannotation.ModelMap
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

@SupportedAnnotationTypes("com.yqh.libannotation.ModelMap")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ModelProcessor : AbstractProcessor() {

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        AptContext.init(processingEnv)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        println("********************* model start *********************")

        roundEnv.getElementsAnnotatedWith(ModelMap::class.java)
            .forEach { element ->
                //获取注解元素 Element 的 构造器,如果没有，则返回空列表
                element.enclosedElements
                    //过滤出 某个类或接口的方法、构造方法(此处对应)、或初始化程序(静态或实例)，包括注释类型元素，
                    .filterIsInstance<ExecutableElement>()
                    .firstOrNull { executableElement: ExecutableElement ->
                        //返回此元素的简单名称
                        // 例如 Set<E> 的简单名称为: Set
                        //如果此元素表示一个未指定的包，则返回一个空名称
                        //如果它表示一个构造方法，则返回名称 "<init>"
                        //如果它表示一个静态初始化程序，则返回名称"<clinit>"
                        //如果它表示一个匿名类或实例初始化程序，则返回一个空名称
                        executableElement.simpleName() == "<init>"
                    }?.let { it ->
                        val typeElement = element as TypeElement
//                        val clz = ClassName(typeElement.packageName(),typeElement.simpleName())
                        //创建类文件
                        //1.设置包名及类名称
                        FileSpec.builder(
                            typeElement.packageName(),
                            "${typeElement.simpleName()}\$\$ModelMap"
                        )
                            //2.添加方法
                            // fun Sample.toMap() {
                            //      return mapOf("a" to a, "b" to b)
                            //  }
                            .addFunction(
                                FunSpec.builder("toMap")
                                    //设置reveiver， asType 返回元素定义的类型，并且需要再次转为 Kotlin 的类型
                                    .receiver(typeElement.asType().asKotlinTypeName())
                                    //设置方法 body 内容
                                    .addStatement("return mapOf(${it.parameters.joinToString { """"${it.simpleName()}" to ${it.simpleName()}""" }})")
                                    .build()
                            )
                            //添加方法
                            // fun <V> Map<String, V>.toSample() : V {
                            //      return Sample(this["a"] as Int, this["b"] as String)
                            // }
                            .addFunction(
                                //添加方法名称
                                FunSpec.builder("to${typeElement.simpleName()}")
                                    //添加泛型参数
                                    .addTypeVariable(TypeVariableName("V"))
                                    //添加 receiver : Map<String,V>
                                    .receiver(MAP.parameterizedBy(STRING, TypeVariableName("V")))
                                    .addStatement(
                                        "return ${typeElement.simpleName()}(${
                                            it.parameters.joinToString {
                                                """this["${it.simpleName()}"] as %T """
                                            }
                                        })",
                                        *it.parameters.map { it.asType().asKotlinTypeName() }
                                            .toTypedArray()
                                    )
                                    .build()
                            )
                            //添加方法
                            //fun Simple.contrast(info: Simple) = this.name == info.name && this.age == info.age
                            .addFunction(
                                FunSpec.builder("contrast")
                                    .receiver(typeElement.asType().asKotlinTypeName())
                                    .addParameter(
                                        "info",
                                        ClassName(
                                            typeElement.packageName(),
                                            typeElement.simpleName()
                                        )
                                    )
                                    .addStatement(
                                        "return ${
                                            it.parameters.joinToString(separator = "") {
                                                """this.${it.simpleName} == info.${it.simpleName} && """
                                            }.let {
                                                it.substring(0, it.lastIndexOf("&&"))
                                            }
                                        }"
                                    )
                                    .build()
                            )
                            .build().writeToFile()
                    }
            }

        println("********************* model end *********************")
        return true
    }
}