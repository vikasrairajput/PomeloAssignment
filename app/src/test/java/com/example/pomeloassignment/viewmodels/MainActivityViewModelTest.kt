package com.example.pomeloassignment.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.pomeloassignment.CoroutineTestRule
import com.example.mmtassignment.getOrAwaitValue
import com.example.pomeloassignment.source.LocalDataSourceTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.*


class MainActivityViewModelTest {

    private lateinit var viewModel: MainActivityViewModel

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        viewModel= MainActivityViewModel(LocalDataSourceTest(),TestCoroutineDispatcher())

    }

    @ExperimentalCoroutinesApi
    @Test
    fun flightsListSortingTest()= runBlockingTest {
        viewModel.getPickUpsList()
        assertTrue(viewModel.getPickUpsLiveData().getOrAwaitValue().data?.size!!>=0)
        viewModel.getPickUpsLiveData().getOrAwaitValue().data?.forEach {
            assertTrue(it.alias.isNotBlank())
            assertTrue(it.address1.isNotBlank())
            assertTrue(it.active)
            assertTrue(it.city.isNotBlank())
        }
    }


   @ExperimentalCoroutinesApi
   @Test
   fun sortingTest(){
       viewModel.getPickUpsList()
       Thread.sleep(1000)
       viewModel.sortList(29.957846,77.554115,TestCoroutineDispatcher())
       var previousDistance:Double=0.0
       viewModel.getPickUpsLiveData().getOrAwaitValue().data?.forEach {
           assertTrue(it.distance >=previousDistance)
           previousDistance=it.distance
       }
   }
}