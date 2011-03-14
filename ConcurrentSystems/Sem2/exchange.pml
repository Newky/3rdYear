mtype = { offhook, dialtone, number, ringing,
        engaged, connected, hangup, hungup };
//The channel between the two consists of a certain action and a channel.
//
chan conn = [0] of { mtype, chan };


//Exchange 
active proctype exchange()
{       
//This is the channel which is passed between the two as param two
chan who;
//In idle state
idle:   conn?offhook,who;
        {       who!dialtone;
                who?number;
                if
                :: who!engaged; goto zombie
                :: who!ringing ->
                        who!connected;
                        if
                        :: who!hungup; goto zombie
                        :: skip
                        fi
                fi
         } unless
         {      if
                :: who?hangup -> goto idle
                :: timeout -> goto zombie
                fi
          }
zombie: who?hangup; goto idle
}

active proctype subs()
{       

chan me = [0] of { mtype };

idle: conn!offhook,me;
      me?dialtone;
      me!number;
      if
      :: me?engaged
      :: me?ringing ->
              if
              :: me?connected;
                      if
                      :: me?hungup
                      :: timeout
                      fi
              :: skip
              fi
      fi;
      me!hangup; goto idle
}
