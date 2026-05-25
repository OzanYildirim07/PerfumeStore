package com.perfumestore.data;

import com.perfumestore.entity.*;
import com.perfumestore.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ContentRepository contentRepository;
    private final PerfumeRepository perfumeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(CategoryRepository categoryRepository, ContentRepository contentRepository,
                           PerfumeRepository perfumeRepository, UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.categoryRepository = categoryRepository;
        this.contentRepository = contentRepository;
        this.perfumeRepository = perfumeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }
        createAdminUser();
        List<Category> categories = createCategories();
        List<Content> contents = createContents();
        createPerfumes(categories, contents);
    }

    private void createAdminUser() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@luxperfume.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@luxperfume.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRole(User.Role.USER);
        userRepository.save(user);
    }

    private List<Category> createCategories() {
        Category luxury = new Category();
        luxury.setName("Luxury");
        Category designer = new Category();
        designer.setName("Designer");
        Category niche = new Category();
        niche.setName("Niche");
        return categoryRepository.saveAll(Arrays.asList(luxury, designer, niche));
    }

    private List<Content> createContents() {
        List<Content> contents = new ArrayList<>();
        contents.add(createContent("Bergamot", Content.ContentType.MEYVE));
        contents.add(createContent("Siyah Frenk Üzümü", Content.ContentType.MEYVE));
        contents.add(createContent("Elma", Content.ContentType.MEYVE));
        contents.add(createContent("Ananas", Content.ContentType.MEYVE));
        contents.add(createContent("Tarçın", Content.ContentType.BAHARAT));
        contents.add(createContent("Kakule", Content.ContentType.BAHARAT));
        contents.add(createContent("Pembe Biber", Content.ContentType.BAHARAT));
        contents.add(createContent("Sandal Ağacı", Content.ContentType.ODUNSU));
        contents.add(createContent("Sedir Ağacı", Content.ContentType.ODUNSU));
        contents.add(createContent("Vetiver", Content.ContentType.ODUNSU));
        contents.add(createContent("Paçuli", Content.ContentType.ODUNSU));
        contents.add(createContent("Deniz Tuzu", Content.ContentType.FRESH));
        contents.add(createContent("Nane", Content.ContentType.FRESH));
        contents.add(createContent("Lavanta", Content.ContentType.FRESH));
        contents.add(createContent("Kehribar", Content.ContentType.AMBER));
        contents.add(createContent("Vanilya", Content.ContentType.AMBER));
        contents.add(createContent("Misk", Content.ContentType.AMBER));
        contents.add(createContent("Tonka Fasulyesi", Content.ContentType.AMBER));
        return contentRepository.saveAll(contents);
    }

    private Content createContent(String name, Content.ContentType type) {
        Content content = new Content();
        content.setName(name);
        content.setType(type);
        return content;
    }

    private void createPerfumes(List<Category> categories, List<Content> contents) {
        List<Perfume> perfumes = new ArrayList<>();
        
        perfumes.add(createPerfume("Dior Sauvage", "Dior",
                "Bir manifesto niteliğindeki ismiyle dikte edilen, radikal derecede taze bir kompozisyon. Hem ham hem de asil. Aşırı dozlarda kullanılan, olağanüstü bir özenle seçilmiş doğal içerikler hakimdir.",
                new BigDecimal("4110.00"), "https://www.myperfumeshop.com/cdn/shop/files/christian-dior-sauvage-eau-forte-parfum-alcohol-free-464861.png?v=1740783663&width=2048", Perfume.Gender.ERKEK, 100, categories.get(1),
                Arrays.asList(findContent(contents, "Bergamot"), findContent(contents, "Pembe Biber"), findContent(contents, "Vetiver"))));
        
        perfumes.add(createPerfume("Bleu de Chanel", "Chanel",
                "Kalıplara meydan okuyan erkekler için odunsu ve aromatik bir koku. Canlandırıcı narenciye notalarıyla harmanlanmış, derinlemesine şehvetli Eau de Parfum; taze, temiz ve yoğun bir imza sunar.",
                new BigDecimal("5390.00"), "https://www.chanel.com/images/t_one/w_0.51,h_0.51,c_crop/q_auto:good,f_autoplus,fl_lossy,dpr_1.1/w_1240/bleu-de-chanel-parfum-spray-3-4fl-oz--packshot-default-107180-9564892200990.jpg", Perfume.Gender.ERKEK, 100, categories.get(1),
                Arrays.asList(findContent(contents, "Sedir Ağacı"), findContent(contents, "Sandal Ağacı"), findContent(contents, "Nane"))));
        
        perfumes.add(createPerfume("Acqua di Gio Profondo", "Giorgio Armani",
                "Acqua di Gio Profondo, Acqua di Gio'nun yoğun deniz yorumudur. Bir kokudan çok daha fazlası; ruhun derinliklerine doğru büyüleyici bir dalış.",
                new BigDecimal("4000.00"), "https://escentual.com/cdn/shop/files/giorgio_armani_acqua_di_gio_profundo_parfum_spray_100ml.png?v=1759389632&width=1445", Perfume.Gender.ERKEK, 125, categories.get(1),
                Arrays.asList(findContent(contents, "Deniz Tuzu"), findContent(contents, "Bergamot"), findContent(contents, "Misk"))));
        
        perfumes.add(createPerfume("Tom Ford Oud Wood", "Tom Ford",
                "Nadir. Egzotik. Kendine has. Bir parfümörün cephaneliğindeki en nadide, değerli ve pahalı içeriklerden biri olan öd ağacı, genellikle tütsü dolu tapınaklarda yakılır.",
                new BigDecimal("11415.00"), "https://sdcdn.io/tf/tf_sku_T1XG01_2000x2000_0.png", Perfume.Gender.ERKEK, 50, categories.get(2),
                Arrays.asList(findContent(contents, "Sandal Ağacı"), findContent(contents, "Kakule"), findContent(contents, "Vanilya"))));
        
        perfumes.add(createPerfume("Creed Aventus", "Creed",
                "Aventus, gücü, iktidarı ve başarıyı kutlayan tarihi bir imparatorun dramatik hayatından ilham almıştır. 2010 yılında sunulan bu koku, markanın tarihindeki en çok satan parfüm haline geldi.",
                new BigDecimal("14800.00"), "https://cdn.shopify.com/s/files/1/0730/0929/9798/products/Creed-Aventus-Eau-de-Parfum-100ml.png?v=1686154607", Perfume.Gender.ERKEK, 100, categories.get(2),
                Arrays.asList(findContent(contents, "Ananas"), findContent(contents, "Bergamot"), findContent(contents, "Misk"))));
        
        perfumes.add(createPerfume("Chanel No. 5", "Chanel",
                "Şimdi ve sonsuza dek sürecek bir koku. Dişiliğin nihai simgesi. Karakter ve güç bakımından saf parfüme en yakın, zarif ve lüks bir sprey.",
                new BigDecimal("6160.00"), "https://www.sephora.com/productimages/sku/s465690-main-zoom.jpg", Perfume.Gender.KADIN, 100, categories.get(0),
                Arrays.asList(findContent(contents, "Vanilya"), findContent(contents, "Sandal Ağacı"), findContent(contents, "Misk"))));
        
        perfumes.add(createPerfume("Dior J'adore", "Dior",
                "J'adore Eau de Parfum, Dior'un ikonik kadınsı çiçeksi kokusudur. Hem güçlü hem de zarif bir kadının imzası olan, çiçeklerin şehvetli bir buketi.",
                new BigDecimal("5480.00"), "https://www.dior.com/dw/image/v2/BGXS_PRD/on/demandware.static/-/Sites-master_dior/default/dwfa4422d8/Y0615246/Y0615246_background_ZHC.png?sw=3000&sh=1600", Perfume.Gender.KADIN, 100, categories.get(1),
                Arrays.asList(findContent(contents, "Misk"), findContent(contents, "Vanilya"), findContent(contents, "Paçuli"))));
        
        perfumes.add(createPerfume("YSL Black Opium", "Yves Saint Laurent",
                "Black Opium Eau de Parfum, Yves Saint Laurent'ın baştan çıkarıcı ve büyüleyici kadın kokusudur. Adrenalin zengini kahvenin açılış notaları ve vanilyanın tatlı şehveti, beyaz çiçeklerin yumuşaklığına bırakır.",
                new BigDecimal("4790.00"), "https://escentual.com/cdn/shop/files/yves_saint_laurent_black_opium_le_parfum_spray_30ml.png?v=1729190675", Perfume.Gender.KADIN, 90, categories.get(1),
                Arrays.asList(findContent(contents, "Vanilya"), findContent(contents, "Pembe Biber"), findContent(contents, "Sedir Ağacı"))));
        
        perfumes.add(createPerfume("Tom Ford Lost Cherry", "Tom Ford",
                "Lost Cherry, bir zamanlar yasak olan olgulara tam gövdeli bir yolculuk; dışta oyuncu, şekerleme benzeri bir parıltı ile içte lezzetli, etli bir dokunun baştan çıkarıcı zıtlığını ortaya çıkaran kontrast bir koku.",
                new BigDecimal("12800.00"), "https://lh4.googleusercontent.com/proxy/kQQQTdPZe7gb77jlT3KNZDqVYXueilgxGXtrBjg8Xis3l6ZRuWUWZe4CNhPtH3QMYARQDLkoatN6hfcDhGFmGLivPcX92oAN6RvMw6-4djN7qV7l7DUl3wzK0DeLdUxOzJ0yy9wEpyN4Lh-ChycfLjow_8Mqim1CjLC3qbCrRjbjNRZO_6P5Fe1fcVSZJg", Perfume.Gender.KADIN, 50, categories.get(2),
                Arrays.asList(findContent(contents, "Tarçın"), findContent(contents, "Vanilya"), findContent(contents, "Sandal Ağacı"))));
        
        perfumes.add(createPerfume("Gucci Bloom", "Gucci",
                "Gucci'nin çağdaş, çeşitli ve özgün kadınlarının ruhunu yakalayan Bloom, kreatif direktör Alessandro Michele'nin Moda Evi için tasarladığı ilk koku.",
                new BigDecimal("4400.00"), "https://www.myperfumeshop.com/cdn/shop/products/gucci-gucci-bloom-edp-724855.png?v=1609925260&width=2048", Perfume.Gender.KADIN, 100, categories.get(1),
                Arrays.asList(findContent(contents, "Misk"), findContent(contents, "Sandal Ağacı"), findContent(contents, "Vanilya"))));
        
        perfumes.add(createPerfume("Byredo Gypsy Water", "Byredo",
                "Romany yaşam tarzının cazibesi ve efsanesine duyulan hayranlığa dayanan bir koku. Taze toprak, derin ormanlar ve kamp ateşlerinin kokusu, doğaya yakın, özgür ve renkli bir yaşam rüyasını çağrıştırıyor.",
                new BigDecimal("8670.00"), "https://www.skins.nl/media/b1/b6/cf/1726431997/501f8e942cbe60fc5327bb789fc72e55.png?ts=1760475266", Perfume.Gender.UNISEX, 100, categories.get(2),
                Arrays.asList(findContent(contents, "Sandal Ağacı"), findContent(contents, "Vanilya"), findContent(contents, "Bergamot"))));
        
        perfumes.add(createPerfume("Le Labo Santal 33", "Le Labo",
                "Eski Marlboro reklamlarını hatırlıyor musunuz? Geniş bir ovada, mavi akşam gökyüzünün altında ateşin önünde bir adam ve atı. Amerikan Batı ruhunun tanımlayıcı bir simgesi.",
                new BigDecimal("9620.00"), "https://media.johnlewiscontent.com/i/JohnLewis/238090019", Perfume.Gender.UNISEX, 100, categories.get(2),
                Arrays.asList(findContent(contents, "Sandal Ağacı"), findContent(contents, "Sedir Ağacı"), findContent(contents, "Kakule"))));
        
        perfumes.add(createPerfume("Maison Francis Kurkdjian Baccarat Rouge 540", "Maison Francis Kurkdjian",
                "Baccarat Rouge 540 eau de parfum, kristal üreticisinin 250. doğum gününü kutlamak amacıyla Maison Francis Kurkdjian ve Baccarat arasındaki karşılaşmadan doğdu.",
                new BigDecimal("13560.00"), "https://bakhachegroup.com.my/cdn/shop/files/3700559605905_BR540_EXT_70ML_1.png?v=1768975672&width=1946", Perfume.Gender.UNISEX, 70, categories.get(2),
                Arrays.asList(findContent(contents, "Tarçın"), findContent(contents, "Sedir Ağacı"), findContent(contents, "Kehribar"))));
        
        perfumes.add(createPerfume("Jo Malone Wood Sage & Sea Salt", "Jo Malone",
                "Rüzgarlı sahiller boyunca günlük hayattan kaçış. Sarp kayalıkların mineral kokusuyla canlanan, adaçayının odunsu topraksılığı ile karışan; canlı, coşkulu ve tamamen neşeli bir koku.",
                new BigDecimal("3540.00"), "https://sdcdn.io/jm/jm_sku_L41501_3000x3000_0S.png", Perfume.Gender.UNISEX, 100, categories.get(1),
                Arrays.asList(findContent(contents, "Deniz Tuzu"), findContent(contents, "Nane"), findContent(contents, "Misk"))));
        
        perfumes.add(createPerfume("Diptyque Philosykos", "Diptyque",
                "Pelion Dağı'nda bir Yunan yazının anısı. Denize ulaşmak için yaban incir ağaçlarının oluştur跚duğu doğal bir koruluktan geçmek gerekiyordu. Zirvedeki güneş toprağı ısıtırken, kuru rüzgar ağaçların ve meyvelerin kokusunu taşır.",
                new BigDecimal("7200.00"), "https://abanuc.com/cdn/shop/products/3700431416384-1PHILOSYKOSEDPFRONT.png?v=1624203651", Perfume.Gender.UNISEX, 100, categories.get(2),
                Arrays.asList(findContent(contents, "Lavanta"), findContent(contents, "Sedir Ağacı"), findContent(contents, "Misk"))));
                
        perfumeRepository.saveAll(perfumes);
    }
    

    private Perfume createPerfume(String name, String brand, String description, BigDecimal price,
                                    String imageUrl, Perfume.Gender gender, int volumeMl,
                                    Category category, List<Content> contents) {
        Perfume perfume = new Perfume();
        perfume.setName(name);
        perfume.setBrand(brand);
        perfume.setDescription(description);
        perfume.setPrice(price);
        perfume.setImageUrl(imageUrl);
        perfume.setGender(gender);
        perfume.setVolumeMl(volumeMl);
        perfume.setCategory(category);
        perfume.setContents(contents);
        return perfume;
    }

    private Content findContent(List<Content> contents, String name) {
        return contents.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(contents.get(0));
    }
}
