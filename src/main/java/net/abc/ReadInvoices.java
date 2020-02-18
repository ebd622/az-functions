package net.abc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadInvoices {

    @FunctionName("ReadInvoices")
    //@StorageAccount("chapterbillingstorageacc")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,

            @CosmosDBInput(name = "database",
                    databaseName = "BillingService",
                    collectionName = "invoices",
                    connectionStringSetting = "connectionToCosmos") List<String> inputItems,
                    //connectionStringSetting = "connectionToCosmos") String[] inputItems,

            final ExecutionContext context) {


        context.getLogger().info("CosmostDB items: " + inputItems);


        return request.createResponseBuilder(HttpStatus.OK)
                .body(inputItems)
                .build();
    }
}

