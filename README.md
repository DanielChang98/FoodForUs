# Food For Us
A food donation application to solve the food waste problem in our community, at the same time helping the needy ones.

## Getting Started
The project is written in Java, for Android users.

### Prerequisites
To run the application, your phone must be running Android OS, with minimum SDK 16.
Once successfully run, you will have to register an account for login purposes.
The application requires users to enable their access location permission so that their location can be recorded during delivery of food
to the elderly.

## Features of the application
The bottom navigation menu navigates to 5 pages, named Profile, Bonus, Home, Forum and Notifications.
### Home Page
Successful login will lead you to the home page of the application, where features like Donate Food, Get Food, Register Elderly,
and Deliver Food will be available.<br>

<pre>
<b>Donate Food</b>      : You can fill in a form and submit the details of food to donate.
<b>Get Food</b>         : You can get food from other users by selecting the food in a map view or list view,
                   followed by submitting a pick up schedule, to inform the donors about pick up details.
<b>Register Elderly</b> : You can help to register an elderly to the system by fill in a form and submit, 
                   so that their details especially location is saved.
<b>Deliver Food</b>     : You can deliver the food by selecting the elderly, where Google Map application
                   will pop out for navigation, after getting your permission.
</pre>

### Bonus Page
This page will have features like Redeem Vouchers and Your Vouchers. 
<pre>
<b>Redeem Vouchers</b>  : You can redeem vouchers using bonus points gained upon successful delivery of food
                   to the elderly.
<b>Your Vouchers</b>    : You can view vouchers redeemed and their details. 
                   By selecting the vouchers, you will be prompted for confirmation of using it.
</pre>

### Profile Page
This page will show you your profile like username and email.

### Forum Page
This page will allow you to post forum entries to be seen by all users. Title and comments is needed for a new post.

### Notifications Page
This page will allow you to view notifications of the application.

## Built With
* [Android Studio](https://developer.android.com/studio) 
  - The IDE used.
* [Firebase Cloud Firestore](https://firebase.google.com/) 
  - The database for storing data.
  - For user authentication.
* [Google Cloud Platform](https://cloud.google.com/) 
  - For Maps SDK Android, used to show the map in the application. 
  - For Geocoding API, to convert the address to geographic coordinates.

## Authors
**App Development Team Members** 
- Wong Hao Jie
- Chang Phang Wei
- Kang Yi Qing
- Khor Ying Jie

## Acknowledgements
* Special thanks to CodingWithMitchs - a good reference for Introduction to the Google Maps and Directions API.
(https://www.youtube.com/watch?v=RQxY7rrZATU&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=2&t=132s)
* Created my free logo at LogoMakr.com
