package com.example.testingdemo

import androidx.lifecycle.testing.TestLifecycleOwner
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val vm = SomeViewModel()

    @Test
    fun initializion_isCorrect() {
        assertEquals(vm.data.value, 3.0)
    }

    //Won't work as an un-instrumented test
//    @Test
//    fun update_isCorrect(){
//        val lifecycleOwner = TestLifecycleOwner()
//        vm.data.observe(lifecycleOwner){
//            assertEquals(it, 5.0, 0.0001)
//        }
//        vm.setDataClamped(5.0)
//        vm.data.removeObservers(lifecycleOwner)
//        vm.data.observe(lifecycleOwner){
//            assertEquals(it, 0.0, 0.0)
//        }
//        vm.setDataClamped(-100.0)
//
//    }

    @Test
    fun update_withoutObserve(){
        vm.setDataClamped(-100.0)
        assertEquals(vm.data.value!!, 0.0, 0.0)
    }
}