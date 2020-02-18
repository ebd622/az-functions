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

public class GenerateBillingItemFunc {

    @FunctionName("GenerateBillingItemFunc")
    //@StorageAccount("chapterbillingstorageacc")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            @BlobInput(
                    name = "file",
                    dataType = "binary",
                    path = "chapterbillingblobcontainer/{name}",
                    connection = "connectionToBlob") byte[] content,
            @CosmosDBInput(name = "database",
                    databaseName = "BillingService",
                    collectionName = "items",
                    connectionStringSetting = "connectionToCosmos") String[] inputItems,
            @TableOutput(name = "billingItem",
                    partitionKey = "billingItems",
                    tableName = "BillingItemTbl",
                    connection = "billingAccount") OutputBinding<List<BillingItem>> billingItemsStorage,
            @QueueOutput(name = "message",
                    queueName = "bill-msg",
                    connection = "billingAccount") OutputBinding<List<String>> messagesStorage,
            final ExecutionContext context) {

        if (content == null) {
            context.getLogger().info("File doesn't exist");
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .body("File doesn't exist")
                    .build();
        }

        String contentStr = new String(content, StandardCharsets.UTF_8);
        context.getLogger().info("File content: " + contentStr);

        context.getLogger().info("InputItem : " + inputItems);

        //Split Items per productName
        Map<String, Item> productById = Stream.of(inputItems)
                .map(jsonString -> {
                    Gson gg = new Gson();
                    return gg.fromJson(jsonString, Item.class);
                })
                .collect(Collectors.toMap(Item::getId, Function.identity()));

        //create billingItems and store them in Azure Table
        List<BillingItem> billingItems = Stream.of(contentStr.split("\\r?\\n"))
                .map(str -> str.split(";"))
                .map(str -> {
                    Item item = productById.get(str[2]);
                    return new BillingItem(str[0], str[0], str[1], item.getProductName(), item.getPrice());
                })
                .collect(Collectors.toList());

        context.getLogger().info("Biling items: " + billingItems);

        billingItemsStorage.setValue(billingItems);

        //create messages and add them to Azure Queue
        List<String> messages = billingItems.stream()
                .map(item -> {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("message", "monthlyBilling");
                    jo.addProperty("messageId", item.getId());
                    return jo.toString();
                }).collect(Collectors.toList());
        messagesStorage.setValue(messages);

        return request.createResponseBuilder(HttpStatus.OK)
                .body("Billing items sent.")
                .build();
    }
}

