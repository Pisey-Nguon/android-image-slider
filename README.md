[![](https://www.jitpack.io/v/Pisey-Nguon/android-image-slider.svg)](https://www.jitpack.io/#Pisey-Nguon/android-image-slider)
# ImageSliderView

`ImageSliderView` is a custom Android view component that enables an image slider with auto-scroll functionality. It also supports the addition of a page indicator for enhanced user experience.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Customization](#customization)
- [License](#license)

## Features

- **Auto Scroll**: Automatically scrolls through images after a specified delay.
- **Customizable Margins and Corners**: Allows customization of horizontal margins and corner radius for each image.
- **Page Indicator Support**: Works seamlessly with a page indicator to show the current position in the slider.
- **Smooth Scrolling**: Provides smooth transitions between images.

## Installation

### 1. Add the JitPack Repository

To use the `ImageSliderView` in your project, add the JitPack repository to your root `build.gradle` file:

```setting.gradle
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://www.jitpack.io' }
		}
	}
```

### 2. Add the Dependency

Include the dependency in your module's `build.gradle` file. Replace `Tag` with the specific version or commit hash you want to use:

```groovy
    implementation 'com.github.YourUsername:ImageSliderView:Tag'
```

### 3. Sync Your Project

Sync your project to download the dependencies.

### 4. Add the View to Your XML Layout

Include the `ImageSliderView` in your XML layout as shown below:

```xml
<com.digitaltalent.image_slider.ImageSliderView
    android:id="@+id/imageSlider"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

Additionally, you can add a `PageIndicator` for visual feedback of the current position:

```xml
<com.digitaltalent.page_indicator.PageIndicator
    android:id="@+id/pageIndicator"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom"
    app:circleColor="#777777"
    app:selectedCircleColor="#FFFFFF"
    app:circleRadius="8dp"
    app:circleSpacing="10dp"
/>
```

## Usage

### 1. Basic Usage

In your `Activity` or `Fragment`, configure the `ImageSliderView` as follows:

```kotlin
val imageSliderView: ImageSliderView = findViewById(R.id.imageSlider)

val images = listOf(
    "image1_url",
    "image2_url",
    "image3_url"
)

imageSliderView.setImages(
    images = images,
    onSnapPositionChangeListener = object : OnSnapPositionChangeListener {
        override fun onSnapPositionChange(position: Int) {
            // Update your PageIndicator or perform any other action on position change
        }
    }
)
```

### 2. Enabling Auto Scroll

To enable auto-scroll with a delay of 3 seconds, for example:

```kotlin
imageSliderView.setAutoScroll(3000L) // 3000 milliseconds = 3 seconds
```

## Customization

### 1. Customizing Item Margins and Corners

You can customize the horizontal margins and the corner radius of each item in the slider:

```kotlin
imageSliderView.setImages(
    images = images,
    itemMarginHorizontal = 8f, // 8dp horizontal margin
    itemCircularRadius = 16f, // 16dp corner radius
    onSnapPositionChangeListener = object : OnSnapPositionChangeListener {
        override fun onSnapPositionChange(position: Int) {
            // Handle position change
        }
    }
)
```

### 2. Page Indicator Customization

Customize the page indicator through XML attributes:

```xml
<com.digitaltalent.page_indicator.PageIndicator
    android:id="@+id/pageIndicator"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom"
    app:circleColor="#777777"
    app:selectedCircleColor="#FFFFFF"
    app:circleRadius="8dp"
    app:circleSpacing="10dp"
/>
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.