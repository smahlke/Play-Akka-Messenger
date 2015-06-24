angular.module('chatApp', [])
  .controller('ChatController', function($scope) {
	  messageList = this;
	  messageList.editValue = 0;
	  messageList.chatpartner = "'kein Benutzer ausgewählt'";
	  
	  messageList.messages = [];
 
	$scope.messageList.addMessage = function(data) {
    	if (data.source == messageList.chatpartner) {
        	//wenn die Nachricht von dem aktuellen Chatpartner kommt, gebe diese in dem Chatfenster aus
    		printOnChatWindow(data);
    	} else {
    		//sonst merke Dir diese Nachricht und erhöhe den Zähler
        	messageList.messages.push(data);
    	}
    };
 
    $scope.messageList.remaining = function() {
      var count = 0;
      angular.forEach(messageList.messages, function() {
        count += 1;
      });
      $scope.$apply(function() {
      	  messageList.editValue = count;
      });
    };
    
    $scope.messageList.setChatPartner = function(partner) {
        $scope.$apply(function() {
        	messageList.chatpartner = partner;
        }); 
   };
   
   $scope.messageList.getChatPartner = function() {
       return messageList.chatpartner;
  };

    
  });