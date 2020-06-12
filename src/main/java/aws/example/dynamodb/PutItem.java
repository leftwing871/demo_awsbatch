/*
https://raw.githubusercontent.com/awsdocs/aws-doc-sdk-examples/master/java/example_code/dynamodb/src/main/java/aws/example/dynamodb/PutItem.java
*/
package aws.example.dynamodb;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Put an item in a DynamoDB table.
 *
 * Takes the name of the table, a name (primary key value) and a greeting
 * (associated with the key value).
 *
 * This code expects that you have AWS credentials set up per:
 * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html
 */
public class PutItem
{
    public static void main(String[] args)
    {
        final String USAGE = "\n" +
                "Usage:\n" +
                "    PutItem <table> <name> [field=value ...]\n\n" +
                "Where:\n" +
                "    table    - the table to put the item in.\n" +
                "    name     - a name to add to the table. If the name already\n" +
                "               exists, its entry will be updated.\n" +
                "Additional fields can be added by appending them to the end of the\n" +
                "input.\n\n" +
                "Example:\n" +
                "    PutItem Cellists Pau Language=ca Born=1876\n";

        if (args.length < 2) {
            System.out.println(USAGE);
            System.exit(1);
        }

        printEnvVars();

        printArgs(args);

        String table_name = args[0];
        String name = args[1] + getUniqueString();
        ArrayList<String[]> extra_fields = new ArrayList<String[]>();

        // any additional args (fields to add to database)?
        for (int x = 2; x < args.length; x++) {
            String[] fields = args[x].split("=", 2);
            if (fields.length == 2) {
                extra_fields.add(fields);
            } else {
                System.out.format("Invalid argument: %s\n", args[x]);
                System.out.println(USAGE);
                System.exit(1);
            }
        }

        System.out.format("Adding \"%s\" to \"%s\"", name, table_name);
        if (extra_fields.size() > 0) {
            System.out.println("Additional fields:");
            for (String[] field : extra_fields) {
                System.out.format("  %s: %s\n", field[0], field[1]);
            }
        }

        HashMap<String,AttributeValue> item_values =
                new HashMap<String,AttributeValue>();

        item_values.put("Name", new AttributeValue(name));

        for (String[] field : extra_fields) {
            item_values.put(field[0], new AttributeValue(field[1]));
        }

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

        try {
            ddb.putItem(table_name, item_values);
        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The table \"%s\" can't be found.\n", table_name);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (AmazonServiceException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done!");
    }

    public static String getUniqueString()
    {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formatDateTime = today.format(formatter);
        return formatDateTime;
    }

    public static void printArgs(String[] args)
    {
        System.out.println( "Begin args==========" );

        for(int i = 0; i< args.length; i++){
            System.out.format("  %s: %s\n", i, args[i]);
        }

        System.out.println( "End args==========" );
    }

    public static void printEnvVars()
    {
        System.out.println( "Begin ENV==========" );

        for (Map.Entry entry: System.getenv().entrySet())
            System.out.println( entry.getKey() + "=" + entry.getValue() );

        System.out.println( "End ENV==========" );

    }

}

/*

PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin
AWS_CONTAINER_CREDENTIALS_RELATIVE_URI=/v2/credentials/d586a831-bf3e-417a-938e-5592e5b68a46
AWS_BATCH_JQ_NAME=batch-test-queue
AWS_BATCH_JOB_ID=a04d69a1-f058-4162-b89b-db713b7c9d7c
AWS_BATCH_JOB_ATTEMPT=1
JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk/jre
AWS_EXECUTION_ENV=AWS_ECS_EC2
ECS_CONTAINER_METADATA_URI=http://169.254.170.2/v3/0b2b8e31-69ad-41ef-8ec1-3a222a3f858e
LANG=C.UTF-8
P_TABLE=AAA
HOSTNAME=ip-172-31-45-131
JAVA_ALPINE_VERSION=8.212.04-r0
LD_LIBRARY_PATH=/usr/lib/jvm/java-1.8-openjdk/jre/lib/amd64/server:/usr/lib/jvm/java-1.8-openjdk/jre/lib/amd64:/usr/lib/jvm/java-1.8-openjdk/jre/../lib/amd64
P_NAME=EHLEE
AWS_BATCH_CE_NAME=batch-test-env
PWD=/
JAVA_VERSION=8u212
HOME=/root
SHLVL=1

*/