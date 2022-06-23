# ActiveDiet Mobile Application
An android application, that will help you get active and be healthier by adding new routines and targets into your life! :dart: </br>
With this app you get all in one: Tracking your daily macro-nutrients and building up your work out program.</br>
Furthermore, we weren't satisfied with only taking care of building muscles and eating tips. Therfore, we added a whole section and futures for people who like or want to be motivated to start running :running:.</br>
## Prerequisites
* ActiveDiet
* AndroidStudio
## Getting Started
* Install the prerequisties
* Clone the project: ```git clone https://github.com/Lkrule/ActiveDiet.git```
* Enable the gps permissions:</br>
1. Click the application-window button. :black_large_square:
2. Click the android symbol that is above the ActiveDiet window. 
3. Click on ```App info``` :arrow_right:	```Permissions``` :arrow_right:	```Location``` & ```Phsical activity```:arrow_right:	```Allow only while using the app```
4. Please insert the parameters that are in the ```Settings``` tab in the main menu. (Name,Gender etc...)
</br></br>
![gitpresentation (2)](https://user-images.githubusercontent.com/56928005/175322864-080bfe7f-b1c6-475d-a0e3-8f75291c6929.png)


## Running the application and daily basis routines
* To start the ActiveDiet app, press the ```Run App```.</br>
* The :house: screen:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. You get to see tips and articles regarding healthy life style: healthy and tasty recipes.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. Articles and more information about working out correctly and safely.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3. Under each category[^1], choose any link in order to explore more data that is related to this category.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4. On the top of the home screen, there are 3 options that are available:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Calculate the macro-nutrients daily basis.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Calculate your BMI.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Search for food that you would like to add into your daily macro-nutrients counter.</br>
## Daily basis tools
* By pressing the `DAILY COUNT` button, you can set up your daily meals, and then calculate your macro-nutrients daily-intake :green_salad:</br>
* By pressing the `CALCULATE BMI` button, you can calculate your BMI, and then you know from which category you can benefit the most! All based on science :muscle:</br>
* By pressing the `FOOD SEARCH` button, you can find any food that is in your local DB and if not, you can always add some new food :poultry_leg:</br>


## Exercises management :weight_lifting:
One of many concerns while working out, is to remember the number of sets, repetitions and even what's the exercise itself.</br>
Working out and getting results is based on progress that our body and mind are doing together, and the more consistent we are, the better!</br>
In order to help you, there is an easy to use exercise list that you can insert/delete/update and view your current exercise list that you want to follow :page_facing_up:.</br>
Please navigation to the exercise list: Press the tab button(located in the top left corner) :arrow_right:	```Gym Exercises```
* All the exercises are saved to a local SQLite DB.
</br>![image](https://user-images.githubusercontent.com/56928005/175301940-ffead271-3081-4282-ae02-bd87de489512.png)

## Running with the ActiveDiet :running_woman:
* This feature is built upon using a GPS- a kotlin service, and a timer to take time of your running session.</br>
* An easy to use running module, no need to use any other app along with this one, all in one.
* In the history tab, you can see your running results- how many km you ran, how many calories were burned, how long you ran and your average speed!</br>
![gitpresentation](https://user-images.githubusercontent.com/56928005/175313094-9d58a4c8-f767-403f-b50f-9ed2e18234a7.png)



## Set a reminder :alarm_clock:
* Navigate to the notification tab by pressing the Tab button, and then choose ```Reminders```
* You can set the name of the reminder, and what ever you want it to say. For example: _"Don't forget to prepare food for tomrrow."_
* After clicking _"SUBMIT NOTIFICATION"_, the next message will be displayed.</br>
* When time is up, a notification will appear and the user will hear a sound when it happens.</br>
![gitpresentation](https://user-images.githubusercontent.com/56928005/175311928-6c8587c7-df03-458a-b593-57316279fc15.png)










[^1]: Please calculate your BMI before choosing any random option so you can see what's more relevant to you.
## UML of the project
![test](https://user-images.githubusercontent.com/17682527/175359444-de9e977d-6fe1-486e-a9d9-6d02006d953f.png)
