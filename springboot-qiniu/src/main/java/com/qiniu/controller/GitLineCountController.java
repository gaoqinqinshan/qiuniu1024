package com.qiniu.controller;

import com.qiniu.entity.Author;
import com.qiniu.serve.AuthorService;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/api/authors")
public class GitLineCountController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/count")
    public String countLines(@RequestParam String repoPath) {
        try {
            Repository repository = new FileRepository(new File(repoPath));
            Git git = new Git(repository);

            Iterable<RevCommit> commits = git.log().call();
            Map<String, Integer> lineCountMap = new HashMap<>();

            for (RevCommit commit : commits) {
                String author = commit.getAuthorIdent().getName();
                int lineCount = getLineCount(repository, commit);
                lineCountMap.merge(author, lineCount, Integer::sum);
            }

            for (Map.Entry<String, Integer> entry : lineCountMap.entrySet()) {
                Author author = new Author();
                author.setAuthor(entry.getKey());
                author.setLines(entry.getValue());
                authorService.saveAuthor(author);
            }

            git.close();
            repository.close();

            return "Line count completed";
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }

    private int getLineCount(Repository repository, RevCommit commit) throws IOException {
        RevWalk revWalk = new RevWalk(repository);
        RevCommit revCommit = revWalk.parseCommit(commit.getId());
        RevTree revTree = revCommit.getTree();
        int lineCount = 0;

        try (TreeWalk treeWalk = new TreeWalk(repository)) {
            treeWalk.addTree(revTree);
            treeWalk.setRecursive(true);

            while (treeWalk.next()) {
                String path = treeWalk.getPathString();
                ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader objectLoader = repository.open(objectId);

                if (objectLoader.getType() == Constants.OBJ_BLOB) {
                    try (InputStream inputStream = objectLoader.openStream()) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            lineCount++;
                        }
                    }
                }
            }
        }

        return lineCount;
    }
}