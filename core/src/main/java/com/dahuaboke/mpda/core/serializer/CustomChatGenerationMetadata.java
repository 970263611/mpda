package com.dahuaboke.mpda.core.serializer;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.util.Assert;

import java.beans.ConstructorProperties;
import java.util.*;

/**
 * auth: dahua
 * time: 2025/12/3 17:17
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"@class"})
public class CustomChatGenerationMetadata implements ChatGenerationMetadata {

    private final Map<String, Object> metadata;
    private final String finishReason;
    private final Set<String> contentFilters;

    @ConstructorProperties({"metadata", "finishReason", "contentFilters"})
    public CustomChatGenerationMetadata(Map<String, Object> metadata, String finishReason, Set<String> contentFilters) {
        Assert.notNull(metadata, "Metadata must not be null");
        Assert.notNull(contentFilters, "Content filters must not be null");
        this.metadata = metadata;
        this.finishReason = finishReason;
        this.contentFilters = new HashSet(contentFilters);
    }

    public <T> T get(String key) {
        return (T) this.metadata.get(key);
    }

    public boolean containsKey(String key) {
        return this.metadata.containsKey(key);
    }

    public <T> T getOrDefault(String key, T defaultObject) {
        return (T) (this.containsKey(key) ? this.get(key) : defaultObject);
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return Collections.unmodifiableSet(this.metadata.entrySet());
    }

    public Set<String> keySet() {
        return Collections.unmodifiableSet(this.metadata.keySet());
    }

    public boolean isEmpty() {
        return this.metadata.isEmpty();
    }

    public String getFinishReason() {
        return this.finishReason;
    }

    public Set<String> getContentFilters() {
        return Collections.unmodifiableSet(this.contentFilters);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.metadata, this.finishReason, this.contentFilters});
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            CustomChatGenerationMetadata other = (CustomChatGenerationMetadata) obj;
            return Objects.equals(this.metadata, other.metadata) && Objects.equals(this.finishReason, other.finishReason) && Objects.equals(this.contentFilters, other.contentFilters);
        } else {
            return false;
        }
    }

    public String toString() {
        return String.format("CustomChatGenerationMetadata[finishReason='%s', filters=%d, metadata=%d]", this.finishReason, this.contentFilters.size(), this.metadata.size());
    }
}
