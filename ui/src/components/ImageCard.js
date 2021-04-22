const ImageCard = () => {
    return (
        <div className="max-w-sm rounded overflow-hidden shadow-lg">
            <img src="https://source.unsplash.com/random" alt="" className="w-full" />

            <div className="px-6 py-4">
                <div  className="py-2 font-bold text-purple-500 text-xl">
                Photo by John Doe
                </div>
                <ul>
                <li>
                <strong >Size: </strong>
                    4000
                </li>
                <li >
                    <strong className="underline">Description</strong>
                    <br/>
                    All those moments will be lost in time.
                </li>
                </ul>
            </div>
            <div className="px-6 py-4">
                <span className="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2">
                #tag1
                </span>
                <span className="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2">
                #tag2
                </span>
                <span className="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2">
                #tag3
                </span>
            </div>

        </div>
    )
}

export default ImageCard
