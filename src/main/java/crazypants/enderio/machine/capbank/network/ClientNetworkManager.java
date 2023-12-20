package crazypants.enderio.machine.capbank.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import crazypants.enderio.machine.capbank.TileCapBank;

public class ClientNetworkManager {

    private static final ClientNetworkManager instance = new ClientNetworkManager();

    static {
        MinecraftForge.EVENT_BUS.register(instance);
    }

    public static ClientNetworkManager getInstance() {
        return instance;
    }

    private final Map<Integer, CapBankClientNetwork> networks = new HashMap<Integer, CapBankClientNetwork>();

    ClientNetworkManager() {}

    public void destroyNetwork(int id) {
        CapBankClientNetwork res = networks.remove(id);
        if (res != null) {
            res.destroyNetwork();
        }
    }

    public void updateState(World world, int id, NetworkState state) {
        CapBankClientNetwork network = getOrCreateNetwork(id);
        network.setState(world, state);
    }

    public void updateEnergy(int id, long energyStored, float avgInput, float avgOutput) {
        CapBankClientNetwork res = networks.get(id);
        if (res == null) {
            return;
        }
        res.setEnergyStored(energyStored);
        res.setAverageIOPerTick(avgInput, avgOutput);
    }

    public CapBankClientNetwork getOrCreateNetwork(int id) {
        CapBankClientNetwork res = networks.get(id);
        if (res == null) {
            res = new CapBankClientNetwork(id);
            networks.put(id, res);
        }
        return res;
    }

    public void addToNetwork(int id, TileCapBank tileCapBank) {
        CapBankClientNetwork network = getOrCreateNetwork(id);
        network.addMember(tileCapBank);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.isRemote) {
            networks.forEach((id, network) -> network.destroyNetwork());
            networks.clear();
        }
    }
}
