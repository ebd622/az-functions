package net.abc;


import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.CosmosDBTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

import java.util.List;

public class CosmosTrigger {

    @FunctionName("CosmosTrigger")
    //@StorageAccount("chapterbillingstorageacc")
    public void run(
            @CosmosDBTrigger(name = "database",
                    collectionName = "invoices",
                    databaseName = "BillingService",
                    connectionStringSetting = "") String message,
            final ExecutionContext context
    ) {

        //message.forEach(str -> context.getLogger().info("DB Content: " + str));
        context.getLogger().info("DB Content: " + message);
    }
}
