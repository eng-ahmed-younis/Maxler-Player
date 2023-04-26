package com.roaa.ahmed_abu_elbukhari.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.play.maxler.presentation.screens.onBoarding.Board
import org.w3c.dom.Text

/*
@BindingAdapter("android:loadCategoryImage")
fun setCategoryImage(view:ImageView,category: MealCategoryObject){
    category.categoryImage.let {
        Glide.with(view.context)
            .load(it)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(view)
    }
}

@BindingAdapter("android:category_name")
fun setCategoryName(view: TextView,category: MealCategoryObject){
    category.categoryName.let {
        view.text = it
    }
}




@BindingAdapter("android:meal_image")
fun setMealImage(view : ImageView,lastMeal: MealObject){

    lastMeal.mealImage.let {
        Glide.with(view.context)
            .load(it)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(view)
    }

}


@BindingAdapter("android:meal_name")
fun setMealName(view: TextView,category: MealObject){
    category.mealName.let {
        view.text = it
    }
}



@BindingAdapter("android:meal_description")
fun setMealDescription(view: TextView,categoryItem: CategoryItem){

    categoryItem.category_description?.let {
        view.text = it
    }
}*/


@BindingAdapter("android:boardImage")
fun setOnBoardingItemImage(view: ImageView,board: Board){

    board.image.let {
        Glide.with(view.context)
            .load(it)
            .into(view)
    }
}

@BindingAdapter("android:board_description")
fun setOnBoardingTextDescription(view: TextView,board: Board){
    board.description.let {
        view.text = view.context.getText(board.description)
    }
}

@BindingAdapter("android:board_title")
fun setOnBoardingTitle(view: TextView,board: Board){
    board.description.let {
        view.text = view.context.getText(board.title)
    }
}


/*@BindingAdapter("android:screen_title")
fun  TextView.setScreenTitle(title:String){
    this.context.applicationContext.a
    this.context.theme == this.context.theme.resources.
    this.text = title.toString()
}*/

/*

@BindingAdapter("android:meal_name_ByCategory")
fun setMealNameByCategory(view:TextView,mealCategoryObject: MealObject){
    mealCategoryObject.mealName.let {
        view.text = it
    }
}

@BindingAdapter("android:meal_image_ByCategory")
fun setMealImageByCategory(view:ImageView,mealCategoryObject: MealObject){
    mealCategoryObject.mealImage.let {
        Glide.with(view.context)
            .load(it)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(view)
    }
}


@BindingAdapter("android:collapsingTitle")
fun setCollapsingTitle(view: CollapsingToolbarLayout,mealDetails: Details){
    mealDetails.meal_name.let {
        view.title = it
    }
}
*/



