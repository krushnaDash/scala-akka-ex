package com.krushna.akkaTypedAndClasical;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;

import java.time.Duration;

public class AkkaClassical {


    class TestActor extends AbstractActor{
        @Override
        public Receive createReceive() {
           return ReceiveBuilder.create()
                   .matchAny( o-> sender().tell("Hello", self()))
            .build();
        }
    }

    public static void main(String[] args) {
        System.out.println("hello");
        ActorSystem system = ActorSystem.create("Hello");
        ActorRef testActor=system.actorOf(Props.create(TestActor.class));
        // talking with Actor
        testActor.tell("anyThing",ActorRef.noSender());
        Patterns.ask(testActor,"anayThing", Duration.ofMillis(3000))
                .whenComplete( (message, failure) -> {
                    if(failure== null)
                        System.out.println(message);
                    else
                        System.err.println(failure);
                });



    }
}
