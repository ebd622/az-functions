package net.abc;

import com.google.gson.Gson;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.*;

public class GenerateInvoiceFunction {

    @FunctionName("GenerateInvoiceFunction")
    //@StorageAccount("chapterbillingstorageacc")
    public void run(
            @QueueTrigger(name = "message",
                    queueName = "bill-msg"
                    ,connection = "billingAccount") String message,
            @TableInput(name = "billingItem",
                    tableName = "BillingItemTbl",
                    partitionKey = "billingItems",
                    rowKey = "{messageId}"
                    ,connection = "billingAccount") BillingItem billingItem,
            @CosmosDBOutput(name = "database",
                    databaseName = "BillingService",
                    collectionName = "invoices",
                    connectionStringSetting = "connectionToCosmos") OutputBinding<String> outputItem,
            final ExecutionContext context
    ) {

        context.getLogger().info("Queue message: " + message);
        context.getLogger().info("Billing Item: " + billingItem.getId() + " " + billingItem.getName());

        //dummy calculation
        double totalCost = billingItem.getPrice() * 30;
        billingItem.setTotalAmount(totalCost);
        billingItem.setId(billingItem.getId() + "_zeki");

        Gson json = new Gson();
        //store billingItem in CosmosDBRead
        outputItem.setValue(json.toJson(billingItem));
    }
}

