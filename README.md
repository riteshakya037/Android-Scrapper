[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a5663af23d2948f2addd1aa1a496c08d)](https://www.codacy.com/app/riteshakya037/Android-Demo?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=riteshakya037/Android-Demo&amp;utm_campaign=Badge_Grade)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=102)](https://github.com/ellerbrock/open-source-badge/)
[![Open Source Love](https://badges.frapsoft.com/os/mit/mit.svg?v=102)](https://github.com/ellerbrock/open-source-badge/)
[![Code Climate](https://codeclimate.com/github/riteshakya037/Android-Demo/badges/gpa.svg)](https://codeclimate.com/github/riteshakya037/Android-Demo)
[![Issue Count](https://codeclimate.com/github/riteshakya037/Android-Demo/badges/issue_count.svg)](https://codeclimate.com/github/riteshakya037/Android-Demo)

# Android Scrapper App

Basically this is a android scrapper with a server-like sevice running in a seperate process as a Service 24/7, which rescrapes in a repeating pattern accoring to the time set. Easily the most complex app that I build and uses most of the Android Components in one way or another.

## Getting Started

Below I have given a list of everything that was used to build this project.

## Built using Libraries

* [Joda-Time Android](https://github.com/dlew/joda-time-android) - Replacement for the Java date and time classes.
* [JSOUP](https://jsoup.org/) - Java library for parsing with real-world HTML.
* [GSON](https://github.com/google/gson) - Java library that can be used to convert Java Objects into their JSON representation.
* [Stetho](https://github.com/facebook/stetho) - Sophisticated debug bridge for Android applications.
* [Apache Commons IO](https://github.com/apache/commons-io) - Library contains utility classes, stream implementations, file filters, file comparators, endian transformation classes, and much more.
* [Butter Knife](https://github.com/JakeWharton/butterknife) - View Injection Library
* [EventBus](EventBus) - Publish/Subscribe event bus optimized for Android.
* [MultiProcessPreference](https://github.com/hamsterksu/MultiprocessPreferences) - Allow use of shared preferences between a different processes of same app.
* [Crashlytics](https://fabric.io/kits/android/crashlytics/summary) - Crash reporting solution.

## Design Patterns

* **Architecture Patterns** - Model-View-Presenter
* **Creational design patterns** - Builder, Singleton, Abstract, Factory Method, Object Pool
* **Structural design patterns** - Adapter, Private Class Data, Facade, Bridge
* **Behavioral design patterns** - Chain of responsibility, Null Object, Observer, Mediator

## Android Components

* Service, IInterface(AIDL), Notification, IBinder
* AlarmManager, PendingIntent
* BroadcastReceiver
* MultiProcesses (android:process)
* SQLiteOpenHelper
* PackageManager
* FileProvider
* Views - RecyclerView, DrawerLayout, NavigationView, CardView, Toolbar, Spinner, TextInputLayout, CoordinatorLayout, ViewPager, ScrollView

## Custom Views

* SquareFrameLayout - View that maintains a constant 1:1 ratio
* NonScrollableViewPager - ViewPager that has option to make it non-scrollable.

## Screenshots
<img src="Screenshots/dashboard.png?raw=true" width="360" height="640">
<img src="Screenshots/navigation_drawer.png?raw=true" width="360" height="640">
<img src="Screenshots/create_grid_dialog.png?raw=true" width="360" height="640">
<img src="Screenshots/grid_layout.png?raw=true" width="360" height="640">
<img src="Screenshots/grid_detail_view.png?raw=true" width="360" height="640">
<img src="Screenshots/grid_calendar_view.png?raw=true" width="360" height="640">
<img src="Screenshots/grid_settings.png?raw=true" width="360" height="640">
<img src="Screenshots/league_team_listing.png?raw=true" width="360" height="640">
<img src="Screenshots/settings_page.png?raw=true" width="360" height="640">
<img src="Screenshots/floating_dialog.png?raw=true" width="360" height="640">

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Disclaimer

This project is uploaded as a demo to showcase various components and patterns and is in no way intended as a tutorial.

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc


[![forthebadge](http://forthebadge.com/badges/built-with-love.svg)](http://forthebadge.com)

