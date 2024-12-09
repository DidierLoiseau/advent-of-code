package org.sedam.aoc;

import java.util.List;

public class Day9 extends Day {
    @Override
    public long part1Long(List<String> input) {
        int[] blocks = input.getFirst().chars().map(i -> i - '0').toArray();
        int file = 0, blockPos = 0, toMove = blocks.length - 1;
        long checksum = 0;

        while (file < toMove) {
            if (file % 2 == 0) {
                int fileId = file / 2;
                for (int i = 0; i < blocks[file]; i++) {
                    checksum += (long) fileId * blockPos;
                    blockPos++;
                }
                file++;
            } else {
                int fileId = toMove / 2;
                while (blocks[toMove] > 0 && blocks[file] > 0) {
                    blocks[toMove]--;
                    blocks[file]--;
                    checksum += (long) fileId * blockPos;
                    blockPos++;
                }
                if (blocks[file] == 0) {
                    file++;
                } else if (blocks[toMove] == 0) {
                    toMove -= 2;
                }
            }
        }
        if (file == toMove) {
            int fileId = toMove / 2;
            while (blocks[toMove]-- > 0) {
                checksum += (long) fileId * blockPos;
                blockPos++;
            }
        }

        return checksum;
    }

    @Override
    public long part2Long(List<String> input) {
        int[] blocks = input.getFirst().chars().map(i -> i - '0').toArray();
        int[] blockStart = new int[blocks.length];
        for (int blockPos = 0, i = 0; i < blocks.length; i++) {
            blockStart[i] = blockPos;
            blockPos += blocks[i];
        }

        long checksum = 0;
        for (int toMove = blocks.length - 1; toMove > 0; toMove -= 2) {
            int fileId = toMove / 2;
            var fileSize = blocks[toMove];
            int destBlock = blockStart[toMove];
            for (int i = 1; i < toMove; i += 2) {
                if (blocks[i] >= fileSize) {
                    destBlock = blockStart[i];
                    blocks[i] -= fileSize;
                    blockStart[i] += fileSize;
                    break;
                }
            }
            for (int j = 0; j < fileSize; j++) {
                checksum += (long) (destBlock + j) * fileId;
            }
        }

        return checksum;
    }
}
