import { useState, useEffect } from "react";
import ImageCard from "./components/ImageCard";
import ImageSearch from "./components/ImageSearch";
import ImageUpload from "./components/ImageUpload";

function App() {
  const [images, setImages] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [term, setTerm] = useState("all");
  const [isUploaded, setIsUploaded] = useState(false);
  useEffect(() => {
    fetch(`http://localhost:8080/image/search/` + term)
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
        setImages(data);
        setIsLoading(false);
        setIsUploaded(false);
      })
      .catch((err) => console.log(err));
  }, [term, isUploaded]);

  return (
    <div className="container mx-auto">
      <ImageSearch searchText={(text) => setTerm(text)} />
      <ImageUpload isUploaded={(isUploaded) => setIsUploaded(isUploaded)} />
      {!isLoading && images.length === 0 && (
        <h1 className="text-5xl text-center mx-auto mt-32">No Images Found</h1>
      )}

      {isLoading ? (
        <h1 className="text-6xl text-center mx-auto mt-32">Loading...</h1>
      ) : (
        <div className="grid grid-cols-4 gap-4">
          {images.map((image) => (
            <ImageCard key={image.imageId} image={image} />
          ))}
        </div>
      )}
    </div>
  );
}

export default App;
