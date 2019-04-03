package top.banner.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import top.banner.models.gamecopywriting.GameCopywriting;
import top.banner.service.game.GameService;

import javax.annotation.Resource;
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


