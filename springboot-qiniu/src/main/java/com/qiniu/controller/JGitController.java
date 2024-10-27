package com.qiniu.controller;

import com.qiniu.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.File;
import java.net.URISyntaxException;

/**
 * @author gao
 * @version 1.0
 * @description: 从github把代码clone到本地
 * @date 2024/10/27 19:54
 */
@RestController
public class JGitController {
    // 把代码clone到本地
    @PostMapping("/clone")
    public R<String> cloneRepository(
            @RequestParam("repoUrl") String repoUrl,
            @RequestParam("localPath") String localPath,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        try {
            // 将本地路径转换为File对象
            File localPathFile = new File(localPath);

            // 克隆仓库到本地路径，不使用认证信息
            Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(localPathFile)
                    .call();

            // 创建认证信息提供者
            UsernamePasswordCredentialsProvider credentialsProvider =
                    new UsernamePasswordCredentialsProvider(username, password);

            // 再次克隆仓库，这次使用认证信息
            Git git = Git.cloneRepository()
                    .setURI(String.valueOf(new URIish(repoUrl))) // 将字符串URL转换为URIish对象
                    .setDirectory(localPathFile)
                    .setCredentialsProvider(credentialsProvider)
                    .call();

            // 返回成功消息
            return R.success("successfully");
        } catch (GitAPIException e) {
            // 处理JGit API异常
            e.printStackTrace();
            return R.error(500,"Failed to clone repository: " + e.getMessage());
        } catch (URISyntaxException e) {
            // 处理URL语法异常
            e.printStackTrace();
            return R.error(501,"Invalid repository URL: " + e.getMessage());
        }
    }
}