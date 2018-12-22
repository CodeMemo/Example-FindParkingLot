# Example-FindParkingLot

## Datasource

http://data.gov.tw/node/26653

## Reference

1. Taiwan Coordinate System Translation

http://blog.ez2learn.com/2009/08/15/lat-lon-to-twd97/

2. Google Maps For Android

https://developers.google.com/maps/documentation/android-sdk/intro

3. Google Directions

https://developers.google.com/maps/documentation/directions/intro    

   ***There is no Directions API for Android***
    
## Requirement

1. newer than Android 4.4(Kit Kat)

2. Create a xml file named "secret.xml" in FindParkingLot\app\src\main\res\values for your **Google API Key**

ex:

```

<resources>
    <string name="GoogleMapsForAndroidAPIKey">YOUR_API_KEY</string>
    <string name="GoogleDirectionAPIKey">YOUR_API_KEY</string>
</resources>

```

## Description

Retrive parking lots in New Taipei city

1. Read JSON data from data.gov.tw when this app open

2. Click on recycycleview will show the detail of parking lot and mark its position on map

3. Drop down can refresh recycycleview

   ***Refresh recycycleview will only reload existing data***

4. Click floating button on map will plan route between your location and position of parking lot

   ***Need GPS opened***

## Preview

![pic](https://github.com/CodeMemo/Example-FindParkingLot/blob/master/image/test.gif)
