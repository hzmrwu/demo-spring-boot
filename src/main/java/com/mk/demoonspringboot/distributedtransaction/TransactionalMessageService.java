package com.mk.demoonspringboot.distributedtransaction;


public interface TransactionalMessageService {

    void sendTransactionalMessage(Destination destination, TxMessage message);

}
