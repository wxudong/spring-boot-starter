import "./Table.sol";

contract Netcon {

    event SaveEvent(int256 ret, string indexed netconid);

    string key = "netcon";
    int256 number = 0;
    mapping(int => string) ids;

    constructor() public {
        // 构造函数中创建t_netcon表
        createTable();
    }

    function createTable() private {
        TableFactory tf = TableFactory(0x1001);
        // 创建表
        tf.createTable("t_netcon", key, "id,netconid,applya,applyb,addr,area,balance");
    }

    function openTable() internal returns (Table) {
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable("t_netcon");
        return table;
    }

    function insert(string memory id,
                    string memory netconid,
                    string memory applya,
                    string memory applyb,
                    string memory addr,
                    string memory area,
                    string memory balance) public returns (int) {
        int ret_code = 0;
        Table table = openTable();
        Entry entry = table.newEntry();
        entry.set("id", id);
        entry.set("netconid", netconid);
        entry.set("applya", applya);
        entry.set("applyb", applyb);
        entry.set("addr", addr);
        entry.set("area", area);
        entry.set("balance", balance);
        int count = table.insert(key, entry);
        if (count == 1) {
            number++;
            ids[number] = id;
            ret_code = 0;
        } else {
            ret_code = -1;
        }
        emit SaveEvent(ret_code, netconid);
        return ret_code;
    }

    function queryByNetconId(string memory value) public view returns (int, string, string, string, string, string, string, string) {
	    // 打开表
	    TableFactory tf = TableFactory(0x1001);
	    Table table = tf.openTable("t_netcon");
	    // 查询
	    Condition condition = table.newCondition();
	    condition.EQ("netconid", value);
	    Entries entries = table.select(key, condition);
	    if (uint(entries.size()) == 0) {
		    return (-1, "", "", "", "", "", "", "");
	    } else {
            Entry entry = entries.get(0);
            return (0, entry.getString("id"),
                       entry.getString("netconid"),
                       entry.getString("applya"),
                       entry.getString("applyb"),
                       entry.getString("addr"),
                       entry.getString("area"),
                       entry.getString("balance"));
	    }
    }

    function queryById(string memory value) public view returns (int, string, string, string, string, string, string, string) {
	    // 打开表
	    TableFactory tf = TableFactory(0x1001);
	    Table table = tf.openTable("t_netcon");
	    // 查询
	    Condition condition = table.newCondition();
	    condition.EQ("id", value);
	    Entries entries = table.select(key, condition);
	    if (uint(entries.size()) == 0) {
		    return (-1, "", "", "", "", "", "", "");
	    } else {
            Entry entry = entries.get(0);
            return (0, entry.getString("id"),
                       entry.getString("netconid"),
                       entry.getString("applya"),
                       entry.getString("applyb"),
                       entry.getString("addr"),
                       entry.getString("area"),
                       entry.getString("balance"));
	    }
    }

    function getNumber() public returns (int) {
        return number;
    }

    function getIdByNumber(int num) public returns (string) {
        return ids[num];
    }
}