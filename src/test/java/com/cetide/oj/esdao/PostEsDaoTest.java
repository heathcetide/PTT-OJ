//package com.cetide.oj.esdao;
//
//import com.yupi.yuoj.model.dto.post.PostEsDTO;
//import com.yupi.yuoj.model.dto.post.PostQueryRequest;
//import com.yupi.yuoj.model.entity.Post;
//import com.cetide.oj.service.PostService;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import javax.annotation.Resource;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//
///**
// * 帖子 ES 操作测试
// *
// * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
// * @from <a href="https://yupi.icu">编程导航知识星球</a>
// */
//@SpringBootTest
//public class PostEsDaoTest {
//
//    @Resource
//    private PostEsDao postEsDao;
//
//    @Resource
//    private PostService postService;
//
//    @Test
//    void test() {
//        PostQueryRequest postQueryRequest = new PostQueryRequest();
//        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Post> page =
//                postService.searchFromEs(postQueryRequest);
//        System.out.println(page);
//    }
//
//    @Test
//    void testSelect() {
//        System.out.println(postEsDao.count());
//        Page<PostEsDTO> PostPage = postEsDao.findAll(
//                PageRequest.of(0, 5, Sort.by("createTime")));
//        List<PostEsDTO> postList = PostPage.getContent();
//        Optional<PostEsDTO> byUserId = postEsDao.findById(1L);
//        System.out.println(byUserId);
//        System.out.println(postList);
//    }
//
//    @Test
//    void testAdd() {
//        PostEsDTO postEsDTO = new PostEsDTO();
//        postEsDTO.setId(2L);
//        postEsDTO.setTitle("test19589019201");
//        postEsDTO.setContent("test喵了个咪的123");
//        postEsDTO.setTags(Arrays.asList("java", "python"));
//        postEsDTO.setThumbNum(1);
//        postEsDTO.setFavourNum(1);
//        postEsDTO.setUserId(1L);
//        postEsDTO.setCreateTime(new Date());
//        postEsDTO.setUpdateTime(new Date());
//        postEsDTO.setIsDelete(0);
//        postEsDao.save(postEsDTO);
//        System.out.println(postEsDTO.getId());
//    }
//
//    @Test
//    void testFindById() {
//        Optional<PostEsDTO> postEsDTO = postEsDao.findById(1L);
//        System.out.println(postEsDTO.map(Object::toString));
//    }
//
//    @Test
//    void testCount() {
//        System.out.println(postEsDao.count());
//    }
//
//    @Test
//    void testFindByCategory() {
//        List<PostEsDTO> postEsDaoTestList = postEsDao.findByUserId(1L);
//        System.out.println(postEsDaoTestList);
//    }
//
//    @Test
//    void testFindByName() {
//        List<PostEsDTO> postEsDaoTestList = postEsDao.findByTitle("1");
//        System.out.println(postEsDaoTestList);
//    }
//}
