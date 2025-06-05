package me.corecraft.paper.api.objects;

import com.ryderbelserion.fusion.paper.FusionPaper;
import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.api.ItemManager;
import me.corecraft.paper.api.PaperUserManager;

public abstract class PaperBase {

    protected final CrazyLobby plugin = CrazyLobby.get();

    protected final FusionPaper fusion = this.plugin.getApi();

    protected final PaperUserManager userManager = this.plugin.getUserManager();

    protected final ItemManager itemManager = this.plugin.getItemManager();

}