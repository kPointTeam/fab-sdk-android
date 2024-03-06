package com.fab.sdk.stories

class StoriesSource {

    fun getStoriesView(
        storiesId: String,
        appKey: String,
        host: String,
        showClose: Boolean
    ): String {
        val url = """
          <div id="kpoint-reels"
              data-vx-key=$appKey
              data-vx-host=$host
              data-vx-type="stories"
              data-vx-params='{
              "videoIds": $storiesId,
            "type":"stories",
            "scalable": true
            }'
            >
          </div>
    
    <script>
         function onNextPage(currentPlayerIndex) {
   console.log("Called pagination for ", currentPlayerIndex);
   let onNextPagePromise = new Promise((resolve,reject) => {
    console.log("inside first");
    window["onNextPagePromiseResolve"+currentPlayerIndex] = resolve;
    console.log("inside second");
   });
   fab_sdk.onNextPage(currentPlayerIndex);
<!--   window.fab_sdk("onNextPage:"+currentPlayerIndex);-->
   return onNextPagePromise;
}

function onNextPageReelsIdsReceived(videoIds, currentPlayerIndex) {
  console.log("Function called from flutter");
  window["onNextPagePromiseResolve"+currentPlayerIndex](videoIds);
}

function getVideoConfig(videoId) {
    console.log("Get video config called", videoId);
    let onLikePromise = new Promise((resolve,reject) => {
    window["getVideoConfigPromiseResolve"+videoId] = resolve;
    });
    fab_sdk.onWidgetDetailsReceived(videoId);
    return onLikePromise;
}

function onWidgetDetailsReceived(widgetDetails, videoId) {
  console.log("onWidgetDetailsReceived");
  var response = {
       personalizationInfo:
          widgetDetails,
  };
  window["getVideoConfigPromiseResolve"+videoId](response);
}


window.setupVX = function (vxPlayer) {
    console.log("events =====>");
    window.reelPlayer = vxPlayer;
    window.reelPlayer.onNextPage =  onNextPage;
    window.reelPlayer.getVideoConfig = getVideoConfig;

    vxPlayer.on("slideChange", (index) => {
    console.log("slideChange", index);
    fab_sdk.onMessageReceived("onSlideChange:"+index);
  });
};
var videoDetails;
document.addEventListener("kpw-onclick-button",(event) => {
  console.log('onLike',event);
  var {events: details} = event.detail;
  console.log('Total like count',details.count);
  console.log('Details = ',JSON.stringify(details));
  console.log('Liked video ID',details.videoid);
  console.log('Comment',details.url);
  videoDetails=details;
  
   if(details.buttonType === "close"){
  console.log('close',details.url);
  fab_sdk.onClose(details.videoid);
  }
  else if(details.url === "like"){
  //console.log('onLikeTapped',event);
  console.log('onLikeTapped',details.url);
  fab_sdk.onLike(details.videoid);
  }
  else if(details.url=="comment"){
  console.log('onCommentTapped',details.url);
  fab_sdk.onComment(details.videoid);
  }
  else if(details.url="share"){
  fab_sdk.onShare(details.videoid);
  }
  console.log("details are===========>",details);
}
 );

function onLikeSuccess(){
console.log("Like Success details are===========>",videoDetails.url);
var likeSuccessDetails = {
    url: videoDetails.url,
    status: "success",
    count: videoDetails.count+1,
    videoid: videoDetails.videoid
  }
  var actionCompleted = new CustomEvent("kpw-on-actionCompleted",{
    detail: likeSuccessDetails
  });
  document.dispatchEvent(actionCompleted)
  console.log("Like Success details are===========>",videoDetails);
}
function onLikeFailure(){
console.log("on like failure");
}

function onCommentSuccess(){
console.log("on comment success",videoDetails.count);
videoDetails.count++
var commentSuccessDetails = {
    url: videoDetails.url,
    status: "success",
    count: parseInt(videoDetails.count),
    videoid: videoDetails.videoid
  }
  var actionCompleted = new CustomEvent("kpw-on-actionCompleted",{
    detail: commentSuccessDetails
  });
  document.dispatchEvent(actionCompleted)
  console.log("Comment Success details are===========>",videoDetails);
}

function onShareSuccess(){
console.log("on share success",videoDetails.count);
videoDetails.count++
var shareSuccessDetails = {
    url: videoDetails.url,
    status: "success",
    count: parseInt(videoDetails.count),
    videoid: videoDetails.videoid
  }
  var actionCompleted = new CustomEvent("kpw-on-actionCompleted",{
    detail: shareSuccessDetails
  });
  document.dispatchEvent(actionCompleted)
  console.log("Share Success details are===========>",videoDetails);
}

function onCommentFailure(){
console.log("on comment failure");
}

function onShareFailure(){
console.log("on share failure");
}

function onCustomCallBack(details){
console.log("on custom callback success");
}  
    </script>
<script type='text/javascript' src='https://assets.zencite.com/orca/media/embed/player-vx.js'></script>
"""

        return url;
    }
}
