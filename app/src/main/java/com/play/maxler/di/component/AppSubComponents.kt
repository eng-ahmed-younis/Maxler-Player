package com.play.maxler.di.component

import com.play.maxler.di.component.MainComponent
import dagger.Module

@Module(
    subcomponents = [
        MainComponent::class
    ]
)
class AppSubComponents {

}