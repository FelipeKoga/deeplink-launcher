import dev.koga.deeplinklauncher.util.ext.installed
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class InstalledTest {

    @Test
    fun `should return true if the program is installed`() {
        assertTrue("java".installed())
    }

    @Test
    fun `should return false if the program isn't installed`() {
        assertFalse("irineu".installed())
    }
}