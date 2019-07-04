package io.sstablelevelreset;

import java.io.IOException;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.cassandra.io.sstable.Component;
import org.apache.cassandra.io.sstable.Descriptor;
import org.apache.cassandra.io.sstable.metadata.MetadataType;
import org.apache.cassandra.io.sstable.metadata.StatsMetadata;

public class SSTableLevelReset {

  @Parameter(names = { "-h", "--help" }, help = true)
  private boolean help;

  @Parameter(
      names = { "-o", "--operation" },
      description = "What to do with SSTables. _list_ or _reset_",
      arity = 1,
      required = true)
  private String operation;

  @Parameter(names = { "-f", "--files" },
      description = "Comma-separated list of SSTables to do the operation on",
      arity = 1,
      required = true)
  private String sstables;

  public static void main(String[] argv) {
    SSTableLevelReset main = new SSTableLevelReset();

    JCommander jcommander = JCommander.newBuilder()
        .addObject(main)
        .build();

    jcommander.parse(argv);

    main.run(jcommander);
  }

  private void run(JCommander jcommander) {

    if (help) {
      jcommander.usage();
      System.exit(0);
    }

    String[] sstableList = sstables.split(",");

    switch (operation) {
      case "list":
        listSSTableLevels(sstableList);
        break;
      case "reset":
        resetSSTableLevels(sstableList);
        break;
      default:
        jcommander.usage();
        break;
    }
  }

  private static void resetSSTableLevels(String[] sstablePaths) {
    try {
      for (String sstablePath : sstablePaths) {
        Descriptor sstable = Descriptor.fromFilename(sstablePath);
        StatsMetadata metadata = (StatsMetadata) sstable.getMetadataSerializer().deserialize(sstable, MetadataType.STATS);
        if (metadata.sstableLevel > 0) {
          sstable.getMetadataSerializer().mutateLevel(sstable, 0);
        }
        else {
          System.err.println("Skipped " + sstable.filenameFor(Component.DATA) + " since it is already on level 0");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void listSSTableLevels(String[] sstablePaths) {
    try {
      for (String sstablePath : sstablePaths) {
        Descriptor sstable = Descriptor.fromFilename(sstablePath);
        StatsMetadata metadata = (StatsMetadata) sstable.getMetadataSerializer().deserialize(sstable, MetadataType.STATS);
        System.out.println(String.format("%d %s", metadata.sstableLevel, sstablePath));
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

}
