package com.qiniu.serve;/**
 * @description: TODO 
 * @author gao
 * @date 2024/10/28 0:36
 * @version 1.0
 */
import com.qiniu.entity.Author;
import com.qiniu.mapper.AuthorMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private final AuthorMapper authorMapper;

    public AuthorService(AuthorMapper authorMapper) {
        this.authorMapper = authorMapper;
    }

    public void saveAuthor(Author author) {
        authorMapper.insert(author);
    }
}
