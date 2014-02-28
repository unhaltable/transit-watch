Pebble Transit
--------------
(working title)

Title ideas:

- NextTTC
- NexTTC
- Transit Watch
- TTC Watch
- FTW (Faster Than Walking)
- FTW^2 (Faster Than Walking: For The Watch)

Pebble watch app to view TTC vehicle ETAs for a list of user-configurable stops.

### Main goals

- Minimal Android app
    - Downloads list of routes and stops using [this Java NextBus API library](https://github.com/elliottsj/nextbusapi)
    - Lets user select and save several stops
    - Configuration is synced with Pebble via [AppMessage or AppSync](https://developer.getpebble.com/2/guides/app-phone-communication.html)
    - Spawns a [background service](http://developer.android.com/training/run-background-service/create-service.html) which listens for a message from the Pebble app (when the user opens the Pebble app and selects a stop)
    - Upon message received from Pebble, the background service fetches the vehicle prediction from NextBus and sends it to the Pebble app
- Pebble app
    - Beautiful UI designed by Eugene (written programatically in C... ugh)
    - Cycle through saved stops via the up/down buttons
    - "Minutes until vehicle arrival" is displayed for the next two vehicles
    - Vehicle ETA updates every 10 seconds
    - Middle button
        - Forces refresh of vehicle predictions?
        - Opens an options menu? (Not sure if we need an options menu yet)
- Video demo

### Stretch goals

- Showcase webpage
- Support other transit agencies (this is actually very easy)
- Geofences: When the user is within a certain radius of any saved stop (e.g. 50 m), the Pebble app will open automatically and display the vehicle prediction.
- Android app finds nearby stops and suggests them to the user
    - NextBus does not provide an API for this; we will need to create a web application server to save all stop locations (these are provided by NextBus), then our Android app can query our server with the user's location, and the server will return nearby stops. We can use the same Java [NextBus library](https://github.com/elliottsj/nextbusapi) on our server by using the [Play framework](http://www.playframework.com/).
- Google Play Store / Pebble app store distribution

### Post-CODE goals

- Integration with Faster Than Walking app

### The plan

We'll use [CloudPebble](https://cloudpebble.net) to collaborate on the Pebble portion of the app. The IDE has integration with GitHub, so we'll be able to commit & pull with repo [github.com/elliottsj/PebbleTransit](https://github.com/elliottsj/PebbleTransit). There's also an auto-pull option which may be useful.

[Android Studio](http://developer.android.com/sdk/installing/studio.html) for the Android portion of the app. Android Studio is preferrable to Eclipse because it uses the Gradle build system, which makes dependency management much easier when collaborating.

Both the Pebble app and the Android app will be in the same repository (*sigh*) because that's how we submit to CODE.

#### Useful links

- [SDK guide](https://developer.getpebble.com/2/)
- [SDK samples](https://github.com/pebble/pebble-sdk-examples)
- [SDK reference](https://developer.getpebble.com/2/api-reference/modules.html)
- [Design resources](https://developer.getpebble.com/2/design/)
- [Android Pebble library](https://developer.getpebble.com/2/mobile-app-guide/android-guide.html/)
- [NextBus XML API](https://github.com/elliottsj/nextbusapi/raw/master/nextbus-docs/NextBusXMLFeed.pdf) (we won't be using the XML directly, but this PDF shows what data is available)

### Development environment setup

##### Pebble

Pebble recommends CloudPebble, but unfortunately its collaboration features are lacking. It does have a feature where it can automatically pull from a GitHub repo and compile the app. This is how I recommend you set up your environment...

Once the hackathon has started and I've committed some starter code:

- Open [CloudPebble](https://cloudpebble.net/ide/)
- Click "Import Project" -> "Import From GitHub"
    - Project name: Pebble Transit
    - GitHub project: https://github.com/elliottsj/pebble-transit
    - Branch: (leave blank for 'master')
- Compiling:
    - Go to the "Compilation" section
    - Enter the IP address I have on my phone (this will change occasionally). ([For example](https://github.com/elliottsj/transit-watch/blob/master/images/Screenshot_2014-02-28-13-37-30.png))
    - Check "Automatically pull changes" and "Automatically build after pulling"
    - Now the app will compile automatically whenever anyone pushes to the repo.
- Clone the [repository](https://github.com/elliottsj/pebble-transit) using your favourite Git client and edit using your favourite text editor.

##### Android

- Install [Android Studio](http://developer.android.com/sdk/installing/studio.html)
- Install [Maven](http://maven.apache.org/download.cgi) (you need this to install the NextBus API into a local maven repo)
- Open Android Studio and import the project from path `transit-watch/android`
- Clone the [NextBus API Java library repo](https://github.com/elliottsj/nextbusapi)
- Open a terminal and change directory to `nextbusapi/adapter`
- Run `mvn install`. This will install the library into your local maven repository.