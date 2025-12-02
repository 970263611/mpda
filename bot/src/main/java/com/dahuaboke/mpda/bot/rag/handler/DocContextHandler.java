package com.dahuaboke.mpda.bot.rag.handler;


import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * auth: dahua
 * time: 2025/9/1 16:25
 */
@Component
public class DocContextHandler {

    private final VectorStore vectorStore;

    public DocContextHandler(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<Document> handler(SearchRequest searchRequest, List<Document> documents,String fileType) {
        ArrayList<Document> docContext = new ArrayList<>();
        //文件名称分组
        Map<String, List<Document>> docsByFileName = documents.stream()
                .collect(Collectors.groupingBy(doc -> (String) doc.getMetadata().get("file_name")));

        for (Map.Entry<String, List<Document>> fileEntity : docsByFileName.entrySet()) {
            String fileName = fileEntity.getKey();
            List<Document> fileDocs = fileEntity.getValue();
            List<Double> pageNumbers = fileDocs.stream()
                    .map(doc -> (Double) doc.getMetadata().get("page_number"))
                    .sorted()
                    .collect(Collectors.toList());
            //获取连续段
            ArrayList<List<Double>> continuousSegments = getLists(pageNumbers);
            //根据连续段查询
            for (List<Double> segment : continuousSegments) {
                Double minPage = segment.get(0);
                Double maxPage = segment.get(segment.size() - 1);
                if (minPage > 1) {
                    List<Document> docPre = requestPage(searchRequest, fileName,fileType, minPage - 1);
                    docContext.addAll(docPre);
                }
                List<Document> docNext = requestPage(searchRequest, fileName, fileType,maxPage + 1);
                docContext.addAll(docNext);
            }
        }
        return docContext;
    }

    private ArrayList<List<Double>> getLists(List<Double> pageNumbers) {
        ArrayList<List<Double>> continuousSegments = new ArrayList<>();
        if (!pageNumbers.isEmpty()) {
            ArrayList<Double> currentSegment = new ArrayList<>();
            currentSegment.add(pageNumbers.get(0));
            for (int i = 1; i < pageNumbers.size(); i++) {
                Double prev = pageNumbers.get(i - 1);
                Double curr = pageNumbers.get(i);
                if (curr - prev == 1) {
                    currentSegment.add(curr);
                } else {
                    continuousSegments.add(currentSegment);
                    currentSegment = new ArrayList<>();
                    currentSegment.add(curr);
                }
            }
            continuousSegments.add(currentSegment);
        }
        return continuousSegments;
    }

    private List<Document> requestPage(SearchRequest searchRequest, String fileName, String fileType, double pageNumber) {
        SearchRequest request = SearchRequest.builder()
                .query(searchRequest.getQuery())
                .topK(searchRequest.getTopK())
                .filterExpression(new Filter.Expression(
                        Filter.ExpressionType.AND,
                        new Filter.Expression(
                                Filter.ExpressionType.AND,
                                new Filter.Expression(
                                        Filter.ExpressionType.EQ, new Filter.Key("file_name"), new Filter.Value(fileName)
                                ),
                                new Filter.Expression(
                                        Filter.ExpressionType.EQ, new Filter.Key("page_number"), new Filter.Value(pageNumber)
                                )
                        ),
                        new Filter.Expression(
                                Filter.ExpressionType.EQ, new Filter.Key("file_type"), new Filter.Value(fileType)
                        )
                ))
                .similarityThreshold(searchRequest.getSimilarityThreshold())
                .build();
        return vectorStore.similaritySearch(request);
    }
}
