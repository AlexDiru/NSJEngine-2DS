import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.LongArray;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 17/06/14
 * Time: 00:00
 * To change this template use File | Settings | File Templates.
 */
public class WADReader {


    public static Wad create(String s) throws IOException {
        return new Wad(s);
    }

    public static class Wall
    {
        public Vertex Start;
        public Vertex End;
        public boolean Hidden;

        public Wall(Vertex start, Vertex end, boolean hidden)
        {
            Start = start;
            End = end;
            Hidden = hidden;
        }
    }

    public static class Vertex
    {
        public int X;
        public int Y;
        public Vertex(int x, int y)
        {
            X = x;
            Y = y;
        }
    }

    public static class Map
    {
        public String Name = "";
        public Lump Things;
        public Lump Linedefs;
        public Lump Sidedefs;
        public Lump Vertexes;
        public Lump Segs;
        public Lump SSectors;
        public Lump Nodes;
        public Lump Sectors;

        public ArrayList<Wall> Walls;

        public Map(ArrayList<Lump> lumps, Lump initialLump)
        {
            Name = initialLump.Name;
        System.out.println(Name);
            int index = lumps.indexOf(initialLump);
            Things = lumps.get(++index);
            Linedefs = lumps.get(++index);
            Sidedefs = lumps.get(++index);
            Vertexes = lumps.get(++index);
            Segs = lumps.get(++index);
            SSectors = lumps.get(++index);
            Nodes = lumps.get(++index);
            Sectors = lumps.get(++index);

            ArrayList<Vertex> vertices = new ArrayList<Vertex>();

            System.out.println(Vertexes.Size);
            for (int i = 0; i < Vertexes.Size - 3; i += 4)
            {
                vertices.add(new Vertex(Parse2ByteInt(Vertexes.Data, i), (Parse2ByteInt(Vertexes.Data, i + 2))));
            }

            //Now iterate through Linedefs and link them to vertices to produce 'Walls'
            Walls = new ArrayList<Wall>();

            for (int i = 0; i < Linedefs.Size; i += 14)
            {
                //Check the flag regarding whether the linedef is shown on automap and the special type being -1
                boolean hidden = GetBit(Linedefs.Data[i + 4], 7) || Parse2ByteInt(Linedefs.Data, i + 10) == 255 || Parse2ByteInt(Linedefs.Data, i + 12) == 255;

                //                    Console.WriteLine(Parse2ByteInt(Linedefs.Data, i));
                Vertex startVert = vertices.get(Parse2ByteInt(Linedefs.Data, i));
                Vertex endVert = vertices.get(Parse2ByteInt(Linedefs.Data, i + 2));
                Walls.add(new Wall(startVert, endVert,false));
            }
        }
    }

    public static boolean GetBit(byte b, int bitNum)
    {
        return (b & (1 << bitNum)) != 0;
    }

    public static class Lump
    {
        public int FilePos;
        public int Size;
        public String Name = "";
        public byte[] Data;

        public Lump(byte[] data, int index)
        {
            //data[index->index+3] = FilePos
            FilePos = ParseInt(data, index);

            //data[index+4->index+7] = Size
            Size = ParseInt(data, index + 4);

            //data[index+8->index+15] = Name
            for (int i = 8; i < 16; i++)
                if (data[index + i] != 0)
                    Name += (char)data[index + i];

            //data[FilePos->FilePos+size] = Data
            Data = new byte[Size];
            for (int i = 0; i < Size; i++)
                Data[i] = data[FilePos + i];
        }

    }


    public static int  ParseInt(byte[] data, int index)
    {
        return (data[index]& 0xFF) | (data[index + 1]& 0xFF) << 8 | (data[index + 2]& 0xFF) << 16 | (data[index + 3]& 0xFF) << 24;
    }

    public static short Parse2ByteInt(byte[] data, int index)
    {
        return (short)( (data[index]& 0xFF) | (data[index + 1]& 0xFF) << 8);
    }

    public static class Wad
    {
        public boolean IsIWAD;
        public int NumLumps;
        public int InfoTableOfs;
        public ArrayList<Lump> Lumps;

        public Wad(String file) throws IOException {
            Lumps = new ArrayList<Lump>();

            byte[] data = FileUtils.readFileToByteArray(new File(file));

            //data[0->3] = "IWAD" or "PWAD"
            IsIWAD = data[0] == 73;

            //data[4->7] = numlumps (little endian)
            NumLumps = ParseInt(data, 4);

            //data[8->11] = infotableofs (little endian)
            InfoTableOfs = ParseInt(data, 8);
            System.out.println(InfoTableOfs);

            int dataIndex = InfoTableOfs;
            for (int i = 0; i < NumLumps; i++)
            {
                Lumps.add(new Lump(data, dataIndex));
                dataIndex += 16;
            }

            //Sort lumps by file position, makes it easier getting the map lumps
            Collections.sort(Lumps, new Comparator<Lump>() {
                @Override
                public int compare(Lump o1, Lump o2) {
                    return Integer.compare(o1.FilePos, o2.FilePos);
                }
            });


        }

        public ArrayList<Map> GetMaps()
        {
            ArrayList<Map> maps = new ArrayList<Map>();

            for (Lump lump : Lumps) {
                if (IsMapName(lump.Name))
                    maps.add(new Map(Lumps, lump));
            }

            return maps;

        }

        public  Boolean IsMapName(String strs)
        {
            return strs.equals("E1M1");
        /*
            char[] str= strs.toCharArray();
            System.out.println(str[0] + str[1] + str[2]);
            System.out.println(str[0] + str[1] + str[2]);

            //ExMx or MAPxx
            if (str.length == 4)
            {
                if (str[0] == 'E' && str[2] == 'M' && Character.isDigit(str[1]) && Character.isDigit(str[3]))
                    return true;
            }
            else if (str.length == 5)
            {
                if (str[0] == 'M' && str[1] == 'A' && str[2] == 'P' && Character.isDigit(str[3]) && Character.isDigit(str[4]))
                    return true;
            }
            return false;*/
        }


    }

}
