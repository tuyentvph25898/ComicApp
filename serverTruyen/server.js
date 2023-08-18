const express = require("express");
const mongoose = require("mongoose");

const app = express();
app.use(express.json());

app.use(express.json()); // Middleware để phân tích cú pháp JSON
app.use(express.urlencoded({ extended: true })); // Middleware để phân tích cú pháp dữ liệu x-www-form-urlencoded

mongoose
  .connect(
    "mongodb+srv://tuyentvph25898:12022003@tuyentv25898.b8gs9co.mongodb.net/serverTruyen",
    {
      useNewUrlParser: true,
      useUnifiedTopology: true,
    }
  )
  .then(() => {
    console.log("đã kết nối tới MongoDB");
  })
  .catch((error) => {
    console.error("lỗi kết nối", error);
  });

//khởi chạy server
const port = 3000;
app.listen(port, () => {
  console.log(`server đang lắng nghe tại cổng ${port}`);
});

const truyentranhSchema = new mongoose.Schema({
    tentruyen: String,
    motangan: String,
    tentacgia: String,
    namxuatban: Number,
    anhbia: String,
    anhnoidung: [{ type: String }]
});
const Truyentranh = mongoose.model("truyentranh", truyentranhSchema);

const nguoidungSchema = new mongoose.Schema({
    username: String,
    password: String,
    email: String,
    fullname: String,
    role: { type: String, default: "user" },
    anhdaidien: { type: String, default: "https://png.pngtree.com/png-clipart/20190520/original/pngtree-vector-users-icon-png-image_4144740.jpg" }
});
const Nguoidung = mongoose.model("nguoidung", nguoidungSchema);

const binhluanSchema = new mongoose.Schema({
    id_truyen: {type: mongoose.Schema.Types.ObjectId, ref: 'truyentranh'},
    id_nguoidung: {type: mongoose.Schema.Types.ObjectId, ref: 'nguoidung'},
    noidung: String,
    thoigian: { type: Date, default: Date.now },
    tennguoidung: String
});
const Binhluan = mongoose.model("binhluan", binhluanSchema);


//API đăng kí
app.post("/api/dangki", async (req, res) => {
  const { username, password, email, fullname } = req.body;

  try {
    // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu chưa
    const existingUser = await Nguoidung.findOne({ username });

    if (existingUser) {
      return res.status(400).json({ error: "Tên người dùng đã tồn tại" });
    }

    // Tạo người dùng mới
    const newUser = new Nguoidung({ username, password, email, fullname, role: "user", anhdaidien: "https://png.pngtree.com/png-clipart/20190520/original/pngtree-vector-users-icon-png-image_4144740.jpg"});

    // Lưu người dùng vào cơ sở dữ liệu
    await newUser.save();

    return res.status(201).json({ message: "Đăng kí thành công" });
  } catch (err) {
    return res.status(500).json({ error: "Không thể đăng kí người dùng" });
  }
});

//API đăng nhập
app.post("/api/dangnhap", async (req, res) => {
  const { username, password } = req.body;

  try {
    // Tìm người dùng trong cơ sở dữ liệu
    const user = await Nguoidung.findOne({ username, password }).exec();

    if (!user) {
      return res.status(401).json({ error: "Tên người dùng hoặc mật khẩu không chính xác" });
    }

    // Đăng nhập thành công
    return res.status(200).json( user);
  } catch (err) {
    return res.status(500).json({ error: "Lỗi máy chủ" });
  }
});

//API lay nguoi dung theo id
app.get("/api/nguoidung/:id", async (req, res) => {
  try {
    const userId = req.params.id;
    const user = await Nguoidung.findById(userId);

    if (!user) {
      return res.status(404).json({ message: "User not found" });
    }

    res.json(user);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Server error" });
  }
});

// API thêm truyện tranh mới
app.post("/api/truyentranh", async (req, res) => {
  const { tentruyen, motangan, tentacgia, namxuatban, anhbia, anhnoidung } = req.body;

  const newTruyenTranh = new Truyentranh({
    tentruyen,
    motangan,
    tentacgia,
    namxuatban,
    anhbia,
    anhnoidung,
  });

  try {
    const savedTruyenTranh = await newTruyenTranh.save();
    return res.status(201).json(savedTruyenTranh);
  } catch (err) {
    return res.status(500).json({ error: "Không thể thêm truyện tranh mới" });
  }
});

// API lấy tất cả truyện tranh
app.get("/api/truyentranh", async (req, res) => {
  try {
    const truyentranhList = await Truyentranh.find({});
    return res.status(200).json(truyentranhList);
  } catch (err) {
    return res.status(500).json({ error: "Lỗi máy chủ" });
  }
});

// API sửa thông tin truyện tranh theo ID
app.put("/api/truyentranh/:id", async (req, res) => {
  const { tentuyen, motangan, tentacgia, namxuatban, anhbia, anhnoidung } = req.body;
  const truyentranhId = req.params.id;

  try {
    const updatedTruyenTranh = await Truyentranh.findByIdAndUpdate(
      truyentranhId,
      { tentuyen, motangan, tentacgia, namxuatban, anhbia, anhnoidung },
      { new: true }
    );
    return res.status(200).json(updatedTruyenTranh);
  } catch (err) {
    return res.status(500).json({ error: "Không thể cập nhật thông tin truyện tranh" });
  }
});

// API xóa truyện tranh theo ID
app.delete("/api/truyentranh/:id", async (req, res) => {
  const truyentranhId = req.params.id;

  try {
    const deletedTruyenTranh = await Truyentranh.findByIdAndRemove(truyentranhId);
    return res.status(200).json(deletedTruyenTranh);
  } catch (err) {
    return res.status(500).json({ error: "Không thể xóa truyện tranh" });
  }
});

// api thêm bình luận
app.post("/api/thembinhluan", async (req, res) => {
  const { id_truyen, id_nguoidung, noidung } = req.body;
  // const id_nguoidung = req.body.user._id; //  _id của người dùng đã đăng nhập được lấy từ đăng nhập trước đó
  const thoigian = new Date(); // Lấy thời gian hiện tại
  const nguoidung = await Nguoidung.findById(id_nguoidung);
  console.log(nguoidung);

  try {
    // Tạo một bình luận mới 
    const newComment = new Binhluan({ id_truyen, id_nguoidung, noidung, thoigian, tennguoidung: nguoidung.fullname });

    // Lưu bình luận mới vào cơ sở dữ liệu
    await newComment.save();

    return res.status(201).json({ message: "Bình luận đã được thêm thành công" });
  } catch (err) {
    return res.status(500).json({ error: "Không thể thêm bình luận" });
  }
});

// API sửa bình luận
app.put("/api/binhluan/:id", async (req, res) => {
  try {
    const commentId = req.params.id;
    const updatedContent = req.body.noidung;
console.log(req.body);
    // Tìm bình luận trong cơ sở dữ liệu
    const comment = await Binhluan.findById(commentId);

    // So sánh id_nguoidung của bình luận với người dùng đã đăng nhập
    // if (comment.id_nguoidung.toString() !== req.body.id_nguoidung) {
    //   return res.status(403).json({ success: false, message: "Bạn không được phép sửa bình luận này" });
    // }

    // Cập nhật nội dung bình luận
    comment.noidung = updatedContent;

    // Lưu bình luận đã cập nhật vào cơ sở dữ liệu
    await comment.save();

    return res.json( comment );
  } catch (error) {
    console.error(error);
    return res.status(500).json({ success: false, message: "Không thế cập nhật bình luận" });
  }
});

// API xóa bình luận
app.delete("/api/binhluan/:id", async (req, res) => {
  try {
    const commentId = req.params.id;

    // Tìm bình luận trong cơ sở dữ liệu
    const comment = await Binhluan.findById(commentId);

    // // So sánh id_nguoidung của bình luận với người dùng đã đăng nhập
    // if (comment.id_nguoidung.toString() !== req.body.id_nguoidung) {
    //   return res.status(403).json({ success: false, message: "Bạn không được phép xóa bình luận này" });
    // }

    // Xóa bình luận khỏi cơ sở dữ liệu
    await Binhluan.deleteOne({ _id: commentId });

    return res.json({ success: true, message: "Xóa bình luận thành công" });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ success: false, message: "Không thế xóa bình luận" });
  }
});

// API lấy tất cả bình luận
app.get("/api/binhluan", async (req, res) => {
  try {
    // Lấy tất cả bình luận từ cơ sở dữ liệu
    const comments = await Binhluan.find();
    const tennguoidung = await Nguoidung.findById(Binhluan.id_nguoidung, "fullname")

    return res.json({ success: true, comments, tennguoidung });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ success: false, message: "Lỗi khi lấy bình luận" });
  }
});

// API lấy các bình luận cho một 'id_truyen'
app.get('/api/binhluan/:id_truyen', async (req, res) => {
  try {
    const { id_truyen } = req.params;

    // Tìm tất cả các tài liệu 'Binhluan' khớp với 'id_truyen' được cung cấp
    // Sử dụng populate để lấy 'fullname' từ bảng 'nguoidung'
    const binhluans = await Binhluan.find({ id_truyen });

    return res.status(200).json(binhluans);
  } catch (error) {
    return res.status(500).json({ error: 'Lỗi máy chủ' });
  }
});

//API lấy bình luận theo id_nguoidung
app.get('/api/binhluan/:id_nguoidung', async (req, res) => {
  try {
    const { id_nguoidung } = req.params;

    // Tìm tất cả các tài liệu 'Binhluan' khớp với 'id_truyen' được cung cấp
    // Sử dụng populate để lấy 'fullname' từ bảng 'nguoidung'
    const binhluans = await Binhluan.find({ id_nguoidung }).populate('id_nguoidung', 'fullname');

    return res.status(200).json(binhluans);
  } catch (error) {
    return res.status(500).json({ error: 'Lỗi máy chủ' });
  }
});