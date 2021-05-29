package com.yqh.libcompiler

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.yqh.libannotation.ActivityDestination
import com.yqh.libannotation.FragmentDestination
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation
import kotlin.math.abs

//@AutoService(Processor.class)
@SupportedAnnotationTypes(
    "com.yqh.libannotation.ActivityDestination",
    "com.yqh.libannotation.FragmentDestination"
)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class NavProcessor : AbstractProcessor() {

    private var msg: Messager? = null
    private val outputFileName = "destination.json"
    private var filer: Filer? = null

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        msg = processingEnv.messager
        filer = processingEnv.filer
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        println("********************* start *********************")

        /**
         *  通过处理器环境上下文分别获取项目中标记的
         *  FragmentDestination::class.java
         *  和
         *  ActivityDestination::class.java
         *  注解。
         *  此目的是为了收集项目中哪些类被这两个注解标记了。
         */
        val fragmentElements = roundEnv.getElementsAnnotatedWith(FragmentDestination::class.java)
        val activityElements = roundEnv.getElementsAnnotatedWith(ActivityDestination::class.java)
        if (fragmentElements.isNotEmpty() || activityElements.isNotEmpty()) {
            val destMap = HashMap<String, JSONObject>()
            handleDestination(fragmentElements, FragmentDestination::class.java, destMap)
            handleDestination(activityElements, ActivityDestination::class.java, destMap)

            //filer.createResource 是创建源文件
            //我们可以指定文件输出的地方
            //StandardLocation.CLASS_OUTPUT：java文件生成class文件的位置，/app/build/intermediates/javac/debug/classes/目录下
            //StandardLocation.SOURCE_OUTPUT：java文件的位置，一般在/ppjoke/app/build/generated/source/apt/目录下
            //StandardLocation.CLASS_PATH 和 StandardLocation.SOURCE_PATH用的不多，指的了这个参数，就要指定生成文件的pkg包名了
            val resource = filer?.createResource(StandardLocation.CLASS_OUTPUT, "", outputFileName)
            val sourcePath = resource?.toUri()?.path
            //打印文件输出的路径
            println("sourcePath : $sourcePath")
            //由于我们想要把json文件生成在app/src/main/assets/目录下,所以这里可以对字符串做一个截取，
            //以此便能准确获取项目在每个电脑上的 /app/src/main/assets/的路径
            val assetPath = resource?.let { path ->
                path.toUri().path.let {
                    it.substring(0, it.indexOf("app") + 4)
                } + "src/main/assets/"
            }
            println("assetPath : $sourcePath")

            //利用fastjson把收集到的所有的页面信息 转换成JSON格式的。并输出到文件中
            val file = File(assetPath).apply {
                if (!exists()) mkdirs()
            }

            File(file, outputFileName).apply {
                if (exists()) delete()
                createNewFile()
                val content = JSON.toJSONString(destMap)
                println("content : $content")
                writeText(content)
            }
        }

        println("********************* end *********************")
        return true
    }

    private fun handleDestination(
        elements: Set<Element>,
        annotations: Class<out Annotation>,
        destMap: java.util.HashMap<String, JSONObject>
    ) {
        elements.forEach { element ->
            //TypeElement 是 Element 的一种类型
            //如果我们的注解标记在了类名上，所以可以强转成 TypeElement，使它得到全类名
            val typeElement = element as TypeElement
            //得到全类名 : com.yqh.navannotations.FragmentDestination
            //或者 : com.yqh.navannotations.ActivityDestination
            val className = typeElement.qualifiedName.toString()
            //此处的 id 不可重复，故使用页面类名的 hashCode 作为id
            val id = abs(className.hashCode())
            //页面的 pageUrl 相当于 隐式跳转中的路由
            var pageUrl: String? = null
            //是否需要登录
            var needLogin = false
            //标记是否为首页第一个展现的页面
            var asStarter = false
            //标记该页面是 Fragment 页面还是 Activity 页面
            var isFragment = false;

            //获取注解类型，并判断注解类型
            when (val annotation = element.getAnnotation(annotations)) {
                is FragmentDestination -> {
                    pageUrl = annotation.pageUrl
                    needLogin = annotation.needLogin
                    asStarter = annotation.asStart
                    isFragment = true
                }
                is ActivityDestination -> {
                    pageUrl = annotation.pageUrl
                    needLogin = annotation.needLogin
                    asStarter = annotation.asStart
                    isFragment = false
                }
            }

            if (destMap.containsKey(pageUrl)) {
//                println("不同的页面不允许使用相同的 pageUrl : $pageUrl ，className : $className")
                msg?.printMessage(
                    Diagnostic.Kind.ERROR,
                    "不同的页面不允许使用相同的 pageUrl : $pageUrl ，className : $className"
                )
            } else {
                val obj = JSONObject().apply {
                    put("id", id)
                    put("pageUrl", pageUrl)
                    put("needLogin", needLogin)
                    put("asStart", asStarter)
                    put("className", className)
                    put("isFragment", isFragment)
                }

                //添加到创建的 map 集合当中
                destMap[pageUrl!!] = obj
            }
        }
    }
}