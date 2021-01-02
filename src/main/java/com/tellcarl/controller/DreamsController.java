package com.tellcarl.controller;

import com.tellcarl.domain.DreamReport;
import com.tellcarl.service.DreamService;
import com.tellcarl.service.PdfService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/dreams")
public class DreamsController
{

    private final DreamService dreamService;
    private final PdfService   pdfService;

    public DreamsController( DreamService dreamService, PdfService pdfService )
    {
        this.dreamService = dreamService;
        this.pdfService = pdfService;
    }

    @PostMapping
    public ResponseEntity<DreamReport> sendInvitations(
            @RequestBody String dreamText
    ) throws Exception
    {
        final DreamReport dreamReport = dreamService.getDreamAnalysis(dreamText);

        pdfService.createPdf(dreamReport);

        return new ResponseEntity<>(dreamReport, HttpStatus.OK);
    }
}
