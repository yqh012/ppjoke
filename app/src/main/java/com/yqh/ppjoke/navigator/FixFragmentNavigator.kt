package com.yqh.ppjoke.navigator

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.lang.reflect.Field
import java.util.ArrayDeque

@Navigator.Name("fixFragment")
class FixFragmentNavigator(
    val context: Context,
    val manager: FragmentManager,
    val containerId: Int
) : FragmentNavigator(context, manager, containerId) {
    private val TAG = "FixFragmentNavigator"

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (manager.isStateSaved) {
            Log.i(
                TAG, "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className.toCharArray()[0] == '.') {
            className = context.packageName + className
        }

        val tag = className.substring(className.lastIndexOf(".") + 1)
        var frag = manager.findFragmentByTag(tag)
        if (frag == null) {
            frag = instantiateFragment(context, manager, className, args)
        }
        frag.arguments = args
        val ft = manager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        }

        val fragments = manager.fragments
        fragments.forEach {
            ft.hide(it)
        }

        if (!frag.isAdded) {
            ft.add(containerId, frag, tag)
        }

        ft.show(frag)

//        ft.replace(mContainerId, frag);
        ft.setPrimaryNavigationFragment(frag);

        @IdRes val destId = destination.id
        var mBackStack: ArrayDeque<Int> =
            FragmentNavigator::class.java.getDeclaredField("mBackStack").let {
                it.isAccessible = true
                it.get(this) as ArrayDeque<Int>
            }

        val initialNavigation = mBackStack.isEmpty()
        // TODO Build first class singleTop behavior for fragments
        val isSingleTopReplacement = navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast().toInt() == destId

        val isAdded: Boolean
        if (initialNavigation) {
            isAdded = true
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                manager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast().toInt()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                );
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            isAdded = false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            isAdded = true
        }
        if (navigatorExtras is Extras) {
            navigatorExtras.sharedElements.forEach {
                ft.addSharedElement(it.key, it.value)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        if (isAdded) {
            mBackStack.add(destId)
            return destination
        } else {
            return null
        }
    }

    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }

}