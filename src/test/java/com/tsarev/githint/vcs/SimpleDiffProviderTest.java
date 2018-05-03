package com.tsarev.githint.vcs;

import com.tsarev.githint.infrastructure.LoadFiles;
import com.tsarev.githint.infrastructure.ReadFileSupportTest;
import com.tsarev.githint.vcs.api.ChangedLines;
import com.tsarev.githint.vcs.api.FileDiffProvider;
import com.tsarev.githint.vcs.common.SimpleDiffProvider;
import org.junit.Assert;
import org.junit.Test;

/**
 * Простой набор тестов для {@link SimpleDiffProvider}.
 */
public class SimpleDiffProviderTest extends ReadFileSupportTest {

    /**
     * Тестируемый получатель разницы.
     */
    private FileDiffProvider diffProvider = new SimpleDiffProvider();

    @Test
    @LoadFiles(firstFile = "diffProviderTests/longSimpleContent1.txt",
            secondFile = "diffProviderTests/longSimpleContent2.txt")
    public void firstTest() {
        ChangedLines changedLines = diffProvider.parseChanges(getFirstContent(), getSecondContent());
        Assert.assertEquals("Должна быть разница в 8 блоков.", 8, changedLines.changedBlocks.size());
    }

    @Test
    @LoadFiles(firstFile = "diffProviderTests/longComplexContent1.txt",
            secondFile = "diffProviderTests/longComplexContent2.txt")
    public void secondTest() {
        ChangedLines changedLines = diffProvider.parseChanges(getFirstContent(), getSecondContent());
        Assert.assertEquals("Должна быть разница в 4 блока.", 4, changedLines.changedBlocks.size());
    }

    @Test
    @LoadFiles(firstFile = "diffProviderTests/shortComplexContent1.txt",
            secondFile = "diffProviderTests/shortComplexContent2.txt")
    public void thirdTest() {
        ChangedLines changedLines = diffProvider.parseChanges(getFirstContent(), getSecondContent());
        Assert.assertEquals("Должна быть разница в 4 блока.", 4, changedLines.changedBlocks.size());
    }

    @Test
    @LoadFiles(firstFile = "diffProviderTests/trivialContent1.txt",
            secondFile = "diffProviderTests/trivialContent2.txt")
    public void fourthTest() {
        ChangedLines changedLines = diffProvider.parseChanges(getFirstContent(), getSecondContent());
        Assert.assertEquals("Должна быть разница в 3 блока.", 3, changedLines.changedBlocks.size());
    }
}
