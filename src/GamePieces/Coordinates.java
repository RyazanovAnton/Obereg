package GamePieces;

public class Coordinates {
    public final File file;
    public final Integer rank;

    public Coordinates(File file, Integer rank) {
        this.file = file;
        this.rank = rank;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Coordinates that = (Coordinates) object;

        if (file != that.file) return false;
        return rank.equals(that.rank);
    }

    @Override
    public int hashCode() {
        int result = file.hashCode();
        result = 31 * result + rank.hashCode();
        return result;
    }
}
