package com.example.demo.infra.batch.config.processor;

import com.example.demo.domain.model.Payment;
import com.example.demo.domain.servjce.TaxPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentTaxProcessor implements ItemProcessor<Payment, Payment> {

    private final TaxPolicy taxPolicy;

    @Override
    public Payment process(Payment item) throws Exception {

        /*TODO
        AQUI EU VOU FAZER UM NOVO OBJETO PARA PAGAMETNO, COMO EU TENHO UM CENÁRIO DE CONCORRÊNCIA; NÃO É EXATAMENTE THREADSAFE
        A SOLUÇÃO É DEIXAR PAYMENT INTOCÁVEL E CRIAR UM OBJETO COM O PAGAMENTO COM TAXA REPROCESSADO

         */
        var taxProcessed = taxPolicy.calculateTax(item.getValue(), item.getDueDate());
        item.applyOverdueTax(taxProcessed);

        return item;

    }

}
