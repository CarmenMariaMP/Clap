package com.clap.model.dataModels;

import java.util.List;

import com.clap.model.ContentCreator;
import com.clap.model.PrivacyRequest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PrivacyRequestData {
    List<PrivacyRequest> privacyRequests;
    ContentCreator contentCreator;
}
