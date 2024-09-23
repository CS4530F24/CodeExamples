package com.example.customviewdemo

import androidx.lifecycle.testing.TestLifecycleOwner
import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ViewModelTest {

    val vm = SimpleViewModel()
    val lifecycelOwner = TestLifecycleOwner()
    @Test
    fun basicToolsViewModelTest(){
        val before = vm.color.value!!
        var callbackFired = false
        vm.color.observe(lifecycelOwner){
            callbackFired = true
        }
        vm.pickColor()
        assertTrue(callbackFired)
        assertNotSame(before, vm.color.value!!)

    }
}