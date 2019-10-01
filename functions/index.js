const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
exports.newPostAdded = functions.firestore
    .document('posts/{postId}')
    .onCreate((snap, context) => {
        const newValue = snap.data();
        const name = newValue.name;
      var postTo = newValue.postTo;
      var message = newValue.details;
      console.log("Name= "+name);
      console.log("PostTo= "+ postTo);
      console.log("Message= "+ message)

      
        const payload = {
            notification: {
              title: name+" added a new post.",
              body: message
            }
          }
        
        return admin.messaging().sendToTopic("B2CNotification", payload);
         
    
      

    });