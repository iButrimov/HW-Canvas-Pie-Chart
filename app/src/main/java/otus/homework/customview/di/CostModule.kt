package otus.homework.customview.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CostModule {

    @Provides
    fun provideResources(@ApplicationContext context: Context) = context.resources


}