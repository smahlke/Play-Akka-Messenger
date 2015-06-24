angular.module('chatApp', [])
  .controller('ChatController', function($scope) {
	  messageList = this;
	  messageList.editValue = 0;
	  
	  messageList.messages = [
      {"message":"grfserfs","source":"abuzik","destination":"smahlke","timestamp":"Tue Jun 23 21:12:11 CEST 2015"},
      {"message":"fsefaef","source":"abuzik","destination":"smahlke","timestamp":"Tue Jun 23 21:12:12 CEST 2015"}
    ];
 
	$scope.messageList.addMessage = function(data) {
    	console.log(data);
    	messageList.messages.push(data);
    	console.log(messageList.messages.length);
    };
 
    $scope.messageList.remaining = function() {
    	console.log("jipii");
      var count = 0;
      angular.forEach(messageList.messages, function() {
        count += 1;
      });
      messageList.editValue = count;
    };
    
    
  });