import org.junit.Test
import kotlin.test.assert

internal class BTTest{
    @Test
    fun TestCreateBT(){
        var bt = BT(10)
        assert(bt.root?.value ?: 1 == 10)
    }

    @Test
    fun TestLCA(){
        var bt = BT(10)
        bt.addValue(5)
        bt.addValue(15)
        bt.addValue(4)
        bt.addValue(6)
        bt.addValue(12)
        bt.addValue(11)
        bt.addValue(14)
        bt.addValue(13)
        assert(bt.LCA(5, 15) == 10)
        assert(bt.LCA(4, 13) == 10)
        assert(bt.LCA(4, 6) == 5)
        assert(bt.LCA(5, 4) == 5)
        assert(bt.LCA(13, 11) == 12)
        assert(bt.LCA(14, 15) == 15)
    }
}