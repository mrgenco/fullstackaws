import { useState } from 'react';

const ImageSearch = ({ searchText }) => {
  const [text, setText] = useState('');

  const onSubmit = (e) => {
    e.preventDefault();
    searchText(text);
  }

  return (
    <div className='max-w-sm rounded overflow-hidden my-10 mx-auto'>
      <form onSubmit={onSubmit} className="w-full max-w-sm">
        <div className="flex px-2 bg-gray-200 items-center border-b border-b-2 border-teal-500 py-2">
        <input onChange={e => setText(e.target.value)} className="appearance-none bg-transparent border-none w-full text-gray-700 mr-3 py-1 px-2 leading-tight focus:outline-none" type="text" placeholder="Search Image Term..." />
        <button className="bg-blue-500 hover:bg-green-700 border-blue-500 hover:border-green-700 text-sm border-4 text-white py-1 px-2 rounded" type="submit">
      Search
      </button>
      </div>
      </form>
		</div>
  )
}

export default ImageSearch;