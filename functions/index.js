const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
exports.sendNotification = functions.database.ref('/activeList/{userId}')//specify the firebase node location inside the ref()
    .onWrite(event =>{
        var dataSnapshot = event.data;
        var str1 = "New List is added by ";
        //var str = str1.concat(dataSnapshot.child("activeList").val().ownerName);
        //console.log(str);
        var token = "cepJgtCxbkY:APA91bGdQ2YZcQmvISd9KXxUZguOH_DVEULUVyRYi3oLWGTA-lQmlLWG9HX1pdhUIv4LsKUV3Y-ByhKTn_2MHaTfHW1UOhuMff0ayYl86AxSqwoNIs-KwExUdSvzx54vPyANy__x4sBw";
        var payload ={
            data : {
                //title : dataSnapshot.child("activeList").val().ownerName,
                //body : dataSnapshot.child("activeList").val().listName
                title : "sam",
                body : "vel"
            }
        }
    return admin.messaging().sendToDevice(token,payload)
        .then(function(response){
            console.log("Successfully sent message",response);
        })
        .catch(function(error){
            console.log("Error sending message",error);
        });
});