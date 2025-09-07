package com.utkarsh.MaterialHub.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.AggregateIterable;
import com.utkarsh.MaterialHub.models.Notes;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SearchQuery {

    private final MongoCollection<Document> collection;

    public SearchQuery(@Value("${MONGO_URL}") String DBurl) {
        MongoClient mongoClient = MongoClients.create(DBurl);
        MongoDatabase database = mongoClient.getDatabase("Material-Hub");
        this.collection = database.getCollection("notes");
    }

    public List<Notes> searchNotes(String query, String course, String semester, int page, int size) {
        List<Document> pipeline = new ArrayList<>();

        if (query != null && !query.isEmpty()) {
            pipeline.add(new Document("$search",
                    new Document("index", "default")
                            .append("text", new Document("query", query)
                                    .append("path", Arrays.asList("notesName", "author", "course", "semester"))))
            );
        }

        if (course != null && !course.isEmpty()) {
            pipeline.add(new Document("$match", new Document("course", course)));
        }

        if (semester != null && !semester.isEmpty()) {
            pipeline.add(new Document("$match", new Document("semester", semester)));
        }

        pipeline.add(new Document("$skip", page * size));
        pipeline.add(new Document("$limit", size));

        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Notes> notesList = new ArrayList<>();
        for (Document doc : result) {
            Notes n = new Notes();
            n.setId(doc.getObjectId("_id").toHexString());
            n.setNotesName(doc.getString("notesName"));
            n.setAuthor(doc.getString("author"));
            n.setCourse(doc.getString("course"));
            n.setSemester(doc.getString("semester"));
            n.setFileUrl(doc.getString("fileUrl"));
            n.setPublish(doc.getDate("publish"));
            notesList.add(n);
        }

        return notesList;
    }

    public long countNotes(String query, String course, String semester) {
        List<Document> pipeline = new ArrayList<>();

        if (query != null && !query.isEmpty()) {
            pipeline.add(new Document("$search",
                    new Document("index", "default")
                            .append("text", new Document("query", query)
                                    .append("path", Arrays.asList("notesName", "author", "course", "semester"))))
            );
        }

        if (course != null && !course.isEmpty()) {
            pipeline.add(new Document("$match", new Document("course", course)));
        }

        if (semester != null && !semester.isEmpty()) {
            pipeline.add(new Document("$match", new Document("semester", semester)));
        }

        pipeline.add(new Document("$count", "total"));

        AggregateIterable<Document> result = collection.aggregate(pipeline);
        Document doc = result.first();
        return doc != null ? doc.getInteger("total").longValue() : 0L;
    }
}
