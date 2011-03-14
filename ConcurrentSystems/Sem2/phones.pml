
//Different Types of message types for the channel
mtype = {
request, 
dialtone, 
number,
ringing,
engaged,
connected,
hungup,
hangup,
}

//Connect is a channel used between the exchange and a subscriber.
chan connect = [0] of {mtype, chan};

active proctype exchange(){

chan recv;

idle: connect?request,recv;
	//Everything here is in response.
	//Send dialtone and get number
	recv!dialtone;
	recv?number;
	//Main Call Logic
	{
	if
	::recv!ringing ->
		recv!connected;
		if
		//Startover if receiver hangs up the phone
		:: recv!hungup; goto startover;
		:: skip
		fi
	::recv!engaged ; goto startover;
	fi;
	}
	//Unless statements in promela are different 
	//After every statement in block 1
	//Before every line executes in the code block above,
	//It checks that none of the lines in the unless blocks are executable
	unless {
		if
		//If the subs has hung up, restart exchange 
		:: recv?hangup -> goto idle;
		//If it reaches a point of no executable code goto startover.
		:: timeout -> goto startover;
		fi
	}
startover: recv?hangup; goto idle;
}


//Subscriber proctype, 
//This will be the first to make contact
active proctype subscriber(){

chan call = [0] of {mtype};

idle : connect!request,call;
	//When we have connected with the exchange all requests go through call chan
	//Look for dialtone
	call?dialtone;
	//If dialtone send number
	call!number;
	if
	//If the exchange is ringing
	:: call?ringing -> 
		if
		:: call?connected ->
			if
			:: call?hungup
			:: timeout
			fi
		//Keep's going until call is connected.
		:: skip
		fi
	//If Engaged, then just loop 
	:: call?engaged ;
	fi;
	//Do hangup and go back 2 beginning.
	call ! hangup ; goto idle;
}
