## Title

"Explore Video UX for Your App"

    FAB is a low-code SDK tailored for large enterprises and consumer brands, enabling transformation of their User experiences (UX) to Video user experiences(VUX).
    FAB SDK empowers enterprises to go live with mobile first video experiences, and integrate Instagram-like Reels, Stories, Carousels in their mobile apps.

## Features

    FAB Video UX :
     * Video Experiences - Reels, Stories
     * Video Layouts - Grids, Carousels
     * Interactive Video CTAs
     * Video Analytics Dashboard
     * Video personalization

## Getting started

    To get started you just need to integrate sdk and get access of your video reels experience in your application.

## Integration with Android 

    1. Add the following dependency in your app's build.gradle file
    ```gradle
    implementation 'com.github.kPointTeam:fab-sdk-android:1.0.0'
    ```
    2. Add it in your root settings.gradle at the end of repositories:
    ```gradle
    dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

    ```

    2. Import the package
    ```java
    import 'com.fab.sdk.reels.ReelsExperience';
    ```
    


## Usage

    TODO: Tell users how to use the package.

    Initialize the package
    ```java
    ReelsExperience reelsExperience = findViewById<ReelsExperience>(R.id.fabWebView);
    reelsExperience?.setCallbacks(this)
    reelsExperience?.getReelsConfigDetails(createReelsJSONString())
    ```

    Override the reels interface and implement the callbacks
    
    override fun onVideoDetailsFetched(videoId: String) : String {
        // Function converts string into base 64 format
        return getBase64ConvertedString(videoId);
    }
    override fun onLikeClicked(like: String) : Boolean {
        return  false;
    }
    override fun onCommentClicked(comment: String) : Boolean {
        return false;
    }
    override fun onShareClicked(share: String) : Boolean {
        return false;
    }
    override fun onClose(close: String) {
        //Close the reels experience activity and go back to the previous activity
        this.finish()
    }
    
    

## Additional information

    TODO: Add additional information.