package top.xuguoliang.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.common.utils.WeChatUtil;
import top.xuguoliang.models.gamecopywriting.GameCopywriting;
import top.xuguoliang.models.user.User;
import top.xuguoliang.service.game.GameService;
import top.xuguoliang.service.user.UserWebService;
import top.xuguoliang.service.user.web.ArticleStarResultVO;
import top.xuguoliang.service.user.web.AuthorizeVO;
import top.xuguoliang.service.user.web.UserSetPregnancyVO;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;


/**
 * @author jinguoguo
 */
@RestController
@Api(tags = "情绪游戏")
@RequestMapping("/api/game")
public class GameController {

    @Resource
    private GameService gameService;

    @GetMapping
    public GameCopywriting get(@ApiParam("图片url") @RequestParam String url) throws IOException {
        return gameService.game(url);
    }
}


