# Project 2 - *Grid Image Search*

**Grid Image Search** is an android app that allows a user to search for images on web using simple filters. The app utilizes [Google Image Search API](https://developers.google.com/image-search/). Please note that API has been officially deprecated as of May 26, 2011.

Time spent: **25** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **search for images** by specifying a query and launching a search. Search displays a grid of image results from the Google Image API.
* [x] User can click on "settings" which allows selection of **advanced search options** to filter results
* [x] User can configure advanced search filters such as:
  * [x] Size (small, medium, large, extra-large)
  * [x] Color filter (black, blue, brown, gray, green, etc...)
  * [x] Type (faces, photo, clip art, line art)
  * [x] Site (espn.com)
* [x] Subsequent searches have any filters applied to the search results
* [x] User can tap on any image in results to see the image **full-screen**
* [x] User can **scroll down to see more images**. The maximum number of images is 64 (limited by API).

The following **optional** features are implemented:

* [x] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [x] Used the **ActionBar SearchView** or custom layout as the query box instead of an EditText
* [ ] User can **share an image** to their friends or email it to themselves
* [x] Replaced Filter Settings Activity with a lightweight modal overlay
* [x] Improved the user interface and experiment with image assets and/or styling and coloring

The following **bonus** features are implemented:

* [x] Used the [StaggeredGridView](https://github.com/f-barth/AndroidStaggeredGrid) to display improve the grid of image results
* [ ] User can [zoom or pan images](https://github.com/MikeOrtiz/TouchImageView) displayed in full-screen detail view

The following **additional** features are implemented:

* [x] Clicking any image gives details about the image. It also links to the webpage and full size image url by opening a browser using *implicit intents*.
* [x] Starting the app searches for a random scenery image and sets that as the background image of the activity
* [x] Saves recent searches and displays them as a list in the main activity. Clicking those list items performs that search again. 
* [x] Used Android's CardView class to show search results within cards having shadows

## Video Walkthrough 

Here's a walkthrough of implemented user stories (It is a really long gif so please refresh the page to see it from the beginning):

<img src='http://i.giphy.com/3oEdv8bUfxqtMSWGas.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />


Screenshot when the phone is not connected to the internet

<img src='http://i.imgur.com/LJoJfls.png' title='Offline Mode' width='' alt='Offline Mode' />


GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Adding background images using picasso was a big challenge. Had to do a bit of reading to know how to implement that.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- StaggeredGridView
- CardView

## License

    Copyright 2015 Himanshu Masand

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.